package com.ai.st.microservice.sinic.modules.deliveries.application.public_change_status;

import com.ai.st.microservice.sinic.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryStatus;
import com.ai.st.microservice.sinic.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.UnauthorizedToChangeDeliveryStatus;
import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

@Service
public class DeliveryPublicStatusChanger implements CommandUseCase<DeliveryPublicStatusChangerCommand> {

    private final DeliveryRepository deliveryRepository;

    public DeliveryPublicStatusChanger(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public void handle(DeliveryPublicStatusChangerCommand command) {

        DeliveryId deliveryId = DeliveryId.fromValue(command.deliveryId());
        DeliveryStatus newStatus = DeliveryStatus.fromValue(command.status().name());

        validateNewStatus(deliveryId, newStatus);

        deliveryRepository.changeStatus(deliveryId, newStatus);
    }

    private void validateNewStatus(DeliveryId deliveryId, DeliveryStatus status) {

        // verify delivery exists
        Delivery delivery = deliveryRepository.search(deliveryId);
        if (delivery == null) {
            throw new DeliveryNotFound(deliveryId.value());
        }

        if (!delivery.isFlat()) {
            throw new UnauthorizedToChangeDeliveryStatus(
                    "No es posible cambiar el estado para las entregas de tipo Archivos Planos");
        }

        boolean isValid;
        switch (status.value()) {
        case SENT_CADASTRAL_AUTHORITY:
            isValid = delivery.status().isPossibleToChangeStatusToSendCadastralAuthority();
            break;
        case IN_QUEUE_TO_IMPORT:
            isValid = delivery.status().isPossibleToChangeStatusToInQueueToImport();
            break;
        case IMPORTING:
            isValid = delivery.status().isPossibleToChangeStatusToImporting();
            break;
        case SUCCESS_IMPORT:
            isValid = delivery.status().isPossibleToChangeStatusToSuccessImport();
            break;
        case FAILED_IMPORT:
            isValid = delivery.status().isPossibleToChangeStatusToFailedImport();
            break;
        default:
            throw new UnauthorizedToChangeDeliveryStatus(
                    "No se puede cambiar el estado de la entrega a " + status.value().name());
        }

        if (!isValid) {
            throw new UnauthorizedToChangeDeliveryStatus(
                    String.format("No se puede cambiar el estado de la entrega de %s a %s para la entrega %s",
                            delivery.status().value().name(), status.value().name(), deliveryId.value()));
        }

    }

}
