package com.ai.st.microservice.sinic.modules.deliveries.application.remove_delivery;

import com.ai.st.microservice.sinic.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.sinic.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.UnauthorizedToModifyDelivery;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

@Service
public final class DeliveryRemover implements CommandUseCase<DeliveryRemoverCommand> {

    private final DeliveryRepository deliveryRepository;

    public DeliveryRemover(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public void handle(DeliveryRemoverCommand command) {

        DeliveryId deliveryId = DeliveryId.fromValue(command.deliveryId());
        ManagerCode managerCode = ManagerCode.fromValue(command.managerCode());

        verifyPermissions(deliveryId, managerCode);

        //TODO: remove files from delivery

        deliveryRepository.remove(deliveryId);
    }

    private void verifyPermissions(DeliveryId deliveryId, ManagerCode managerCode) {

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
            throw new UnauthorizedToModifyDelivery("No se puede eliminar la entrega, porque la entrega no es borrador.");
        }

    }

}
