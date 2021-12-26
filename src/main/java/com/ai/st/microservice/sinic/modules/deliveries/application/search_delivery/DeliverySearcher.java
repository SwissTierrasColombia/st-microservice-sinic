package com.ai.st.microservice.sinic.modules.deliveries.application.search_delivery;

import com.ai.st.microservice.sinic.modules.deliveries.application.DeliveryResponse;
import com.ai.st.microservice.sinic.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.sinic.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.sinic.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.sinic.modules.shared.application.Roles;
import com.ai.st.microservice.sinic.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.sinic.modules.shared.domain.Service;

@Service
public final class DeliverySearcher implements QueryUseCase<DeliverySearcherQuery, DeliveryResponse> {

    private final DeliveryRepository repository;

    public DeliverySearcher(DeliveryRepository repository) {
        this.repository = repository;
    }

    @Override
    public DeliveryResponse handle(DeliverySearcherQuery query) {

        Delivery delivery = repository.search(new DeliveryId(query.deliveryId()));

        verifyDelivery(query.deliveryId(), delivery);

        if (isManager(query.role())) {
            verifyManagerBelongToDelivery(delivery, query.entityCode());
        }

        if (isCadastralAuthority(query.role())) {
            verifyDeliveryState(delivery);
        }

        return DeliveryResponse.fromAggregate(delivery);
    }

    private void verifyDelivery(Long deliveryId, Delivery delivery) {
        if (delivery == null)
            throw new DeliveryNotFound(deliveryId);
    }

    private void verifyManagerBelongToDelivery(Delivery delivery, Long managerCode) {
        if (!delivery.deliveryBelongToManager(new ManagerCode(managerCode))) {
            throw new UnauthorizedToSearchDelivery();
        }
    }

    private void verifyDeliveryState(Delivery delivery) {
        if (!delivery.isAvailableToCadastralAuthority()) {
            throw new UnauthorizedToSearchDelivery();
        }
    }

    private boolean isManager(Roles role) {
        return role.equals(Roles.MANAGER);
    }

    private boolean isCadastralAuthority(Roles role) {
        return role.equals(Roles.CADASTRAL_AUTHORITY);
    }

}
