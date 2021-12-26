package com.ai.st.microservice.sinic.modules.files.application.remove_file;

import com.ai.st.microservice.sinic.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.sinic.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.UnauthorizedToModifyDelivery;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.sinic.modules.files.domain.File;
import com.ai.st.microservice.sinic.modules.files.domain.FileId;
import com.ai.st.microservice.sinic.modules.files.domain.FileRepository;
import com.ai.st.microservice.sinic.modules.files.domain.exceptions.FileDoesNotBelongToDelivery;
import com.ai.st.microservice.sinic.modules.files.domain.exceptions.FileNotFound;
import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.StoreFile;

@Service
public final class FileRemover implements CommandUseCase<FileRemoverCommand> {

    private final DeliveryRepository deliveryRepository;
    private final FileRepository fileRepository;
    private final StoreFile storeFile;

    public FileRemover(DeliveryRepository deliveryRepository, FileRepository fileRepository, StoreFile storeFile) {
        this.deliveryRepository = deliveryRepository;
        this.fileRepository = fileRepository;
        this.storeFile = storeFile;
    }

    @Override
    public void handle(FileRemoverCommand command) {

        DeliveryId deliveryId = DeliveryId.fromValue(command.deliveryId());
        FileId fileId = FileId.fromValue(command.fileId());
        ManagerCode managerCode = ManagerCode.fromValue(command.managerCode());

        File file = verifyPermissions(deliveryId, fileId, managerCode);

        fileRepository.remove(fileId);

        deleteStorage(file);
    }

    private File verifyPermissions(DeliveryId deliveryId, FileId fileId, ManagerCode managerCode) {

        // verify delivery exists
        Delivery delivery = deliveryRepository.search(deliveryId);
        if (delivery == null) {
            throw new DeliveryNotFound(deliveryId.value());
        }

        File file = fileRepository.search(fileId);
        if (file == null) {
            throw new FileNotFound(fileId.value());
        }

        // verify owner of the delivery
        if (!delivery.deliveryBelongToManager(managerCode)) {
            throw new UnauthorizedToSearchDelivery();
        }

        // verify status of the delivery
        if (!delivery.isDraft()) {
            throw new UnauthorizedToModifyDelivery("No se puede eliminar el archivo, porque la entrega no es un borrador.");
        }

        // verify attachment belong to delivery product
        if (!file.deliveryId().value().equals(deliveryId.value())) {
            throw new FileDoesNotBelongToDelivery(fileId.value(), deliveryId.value());
        }

        return file;
    }

    private void deleteStorage(File file) {

        String pathFile = file.url().value();
        String pathLog = file.log().value();

        storeFile.deleteFile(pathFile);
        storeFile.deleteFile(pathLog);
    }

}
