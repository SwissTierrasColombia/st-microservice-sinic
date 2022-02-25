package com.ai.st.microservice.sinic.modules.deliveries.application.update_delivery;

import com.ai.st.microservice.sinic.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryObservations;
import com.ai.st.microservice.sinic.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.UnauthorizedToModifyDelivery;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.sinic.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.sinic.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

@Service
public final class DeliveryUpdater implements CommandUseCase<DeliveryUpdaterCommand> {

    private final DeliveryRepository deliveryRepository;

    public DeliveryUpdater(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public void handle(DeliveryUpdaterCommand command) {

        DeliveryId deliveryId = DeliveryId.fromValue(command.deliveryId());
        ManagerCode managerCode = ManagerCode.fromValue(command.managerCode());
        DeliveryObservations observations = DeliveryObservations.fromValue(command.observations());

        // verify delivery exists
        Delivery delivery = deliveryRepository.search(deliveryId);
        if (delivery == null) {
            throw new DeliveryNotFound(command.deliveryId());
        }

        // verify owner of the delivery
        if (!delivery.deliveryBelongToManager(managerCode)) {
            throw new UnauthorizedToSearchDelivery();
        }

        // verify status of the delivery
        if (!delivery.isDraft()) {
            throw new UnauthorizedToModifyDelivery("No se puede actualizar la entrega, porque la entrega no es un borrador.");
        }

        deliveryRepository.update(Delivery.create(
                delivery.id(),
                delivery.code(),
                delivery.date(),
                delivery.dateStatus(),
                delivery.manager(),
                delivery.locality(),
                observations,
                delivery.user(),
                delivery.type()
        ));

    }

}
