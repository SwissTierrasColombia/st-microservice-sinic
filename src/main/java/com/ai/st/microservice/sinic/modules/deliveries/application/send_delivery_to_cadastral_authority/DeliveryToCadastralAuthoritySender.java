package com.ai.st.microservice.sinic.modules.deliveries.application.send_delivery_to_cadastral_authority;

import com.ai.st.microservice.sinic.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryStatus;
import com.ai.st.microservice.sinic.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.UnauthorizedToChangeDeliveryStatus;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.UnauthorizedToModifyDelivery;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.sinic.modules.files.domain.File;
import com.ai.st.microservice.sinic.modules.files.domain.FileRepository;
import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.IDatabaseManager;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.IliMessageBroker;

import java.util.List;

@Service
public final class DeliveryToCadastralAuthoritySender
        implements CommandUseCase<DeliveryToCadastralAuthoritySenderCommand> {

    private final DeliveryRepository deliveryRepository;
    private final FileRepository fileRepository;
    private final IDatabaseManager databaseManager;
    private final IliMessageBroker messageBroker;

    public DeliveryToCadastralAuthoritySender(DeliveryRepository deliveryRepository, FileRepository fileRepository,
            IDatabaseManager databaseManager, IliMessageBroker messageBroker) {
        this.deliveryRepository = deliveryRepository;
        this.fileRepository = fileRepository;
        this.databaseManager = databaseManager;
        this.messageBroker = messageBroker;
    }

    @Override
    public void handle(DeliveryToCadastralAuthoritySenderCommand command) {

        DeliveryId deliveryId = DeliveryId.fromValue(command.deliveryId());
        ManagerCode managerCode = ManagerCode.fromValue(command.managerCode());

        List<File> files = fileRepository.findByDeliveryId(deliveryId);

        verifyPermissions(deliveryId, managerCode, files);

        deliveryRepository.changeStatus(deliveryId, new DeliveryStatus(DeliveryStatus.Status.SENT_CADASTRAL_AUTHORITY));

        sendFilesToQueue(deliveryId, files);
    }

    private void verifyPermissions(DeliveryId deliveryId, ManagerCode managerCode, List<File> files) {

        // verify delivery exists
        Delivery delivery = deliveryRepository.search(deliveryId);
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
                    "No se puede enviar la entrega, porque la entrega no es un borrador.");
        }

        verifyDeliveryHaveMinimumOneFile(files);
        verifyAllFilesAreSuccessful(files);
    }

    private void verifyDeliveryHaveMinimumOneFile(List<File> files) {
        long count = files.size();
        if (count == 0) {
            throw new UnauthorizedToChangeDeliveryStatus(
                    "No se puede enviar la entrega porque no tiene cargado ningún archivo.");
        }
    }

    private void verifyAllFilesAreSuccessful(List<File> files) {
        long count = files.stream().filter(File::allowToSendDelivery).count();
        if (count < files.size()) {
            throw new UnauthorizedToChangeDeliveryStatus(
                    "No se puede enviar la entrega porque existen archivos no válidos.");
        }
    }

    private void sendFilesToQueue(DeliveryId deliveryId, List<File> files) {

        Delivery delivery = deliveryRepository.search(deliveryId);
        String schemaName = String.format("sinic_%s_%s", delivery.code().value(), delivery.locality().code().value());

        databaseManager.createSchema(schemaName);
        int index = 1;
        for (File file : files) {
            messageBroker.sendDataToIliProcess(file.uuid(), file.url(), schemaName, index, files.size());
            index++;
        }
    }

}
