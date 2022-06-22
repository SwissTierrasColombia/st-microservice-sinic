package com.ai.st.microservice.sinic.modules.files.application.add_file;

import com.ai.st.microservice.sinic.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.sinic.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.UnauthorizedToModifyDelivery;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.sinic.modules.files.domain.*;
import com.ai.st.microservice.sinic.modules.files.domain.exceptions.NumberFilesExceeded;
import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;
import com.ai.st.microservice.sinic.modules.shared.domain.UserCode;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.DateTime;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.StoreFile;

import java.util.UUID;

@Service
public final class FileAdder implements CommandUseCase<FileAdderCommand> {

    private final DeliveryRepository deliveryRepository;
    private final FileRepository fileRepository;
    private final DateTime dateTime;
    private final StoreFile storeFile;

    private final static int MAXIMUM_FILES_PER_DELIVERY = 15;

    public FileAdder(DeliveryRepository deliveryRepository, FileRepository fileRepository, DateTime dateTime,
            StoreFile storeFile) {
        this.deliveryRepository = deliveryRepository;
        this.fileRepository = fileRepository;
        this.dateTime = dateTime;
        this.storeFile = storeFile;
    }

    @Override
    public void handle(FileAdderCommand command) {

        DeliveryId deliveryId = DeliveryId.fromValue(command.deliveryId());
        ManagerCode managerCode = ManagerCode.fromValue(command.managerCode());
        UserCode userCode = UserCode.fromValue(command.userCode());
        FileObservations observations = FileObservations.fromValue(command.observations());

        FileVersion version = new FileVersion("0.1");
        FileUUID uuid = new FileUUID(UUID.randomUUID().toString());

        verifyPermissions(deliveryId, managerCode);

        String pathUrl = saveFile(deliveryId, command);

        File file = File.createSuccessfulFile(uuid, observations, new FileUrl(pathUrl), version, userCode, deliveryId,
                dateTime, new FileSize(command.size()));

        fileRepository.save(file);
    }

    private void verifyPermissions(DeliveryId deliveryId, ManagerCode managerCode) {

        Delivery delivery = deliveryRepository.search(deliveryId);

        // verify delivery exists
        if (delivery == null) {
            throw new DeliveryNotFound(deliveryId.value());
        }

        // verify owner of the delivery
        if (!delivery.deliveryBelongToManager(managerCode)) {
            throw new UnauthorizedToSearchDelivery();
        }

        // verify status of the delivery
        if (!delivery.isDraft()) {
            throw new UnauthorizedToModifyDelivery(
                    "No se puede agregar archivos, porque la entrega no esta un borrador.");
        }

        // verify count attachments per product
        long count = fileRepository.findByDeliveryId(deliveryId).size() + 1;
        if (count > MAXIMUM_FILES_PER_DELIVERY) {
            throw new NumberFilesExceeded(MAXIMUM_FILES_PER_DELIVERY);
        }

    }

    private String saveFile(DeliveryId deliveryId, FileAdderCommand command) {
        String namespace = buildNamespace(deliveryId);
        return storeFile.storeFilePermanently(command.bytes(), command.extension(), namespace);
    }

    private String buildNamespace(DeliveryId deliveryId) {
        return String.format("/sinic/entregas/%d", deliveryId.value());
    }

}
