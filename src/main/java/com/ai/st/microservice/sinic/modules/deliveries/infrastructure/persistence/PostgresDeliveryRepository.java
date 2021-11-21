package com.ai.st.microservice.sinic.modules.deliveries.infrastructure.persistence;

import com.ai.st.microservice.sinic.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryStatus;
import com.ai.st.microservice.sinic.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.sinic.modules.deliveries.infrastructure.persistence.jpa.DeliveryJPARepository;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.DeliveryEntity;
import com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities.DeliveryStatusEnum;
import org.springframework.stereotype.Service;

@Service
public final class PostgresDeliveryRepository implements DeliveryRepository {

    private final DeliveryJPARepository deliveryJPARepository;

    public PostgresDeliveryRepository(DeliveryJPARepository deliveryJPARepository) {
        this.deliveryJPARepository = deliveryJPARepository;
    }

    @Override
    public void save(Delivery delivery) {

        DeliveryEntity deliveryEntity = new DeliveryEntity();

        deliveryEntity.setCode(delivery.code().value());
        deliveryEntity.setCreatedAt(delivery.date().value());
        deliveryEntity.setDateStatusAt(delivery.dateStatus().value());
        deliveryEntity.setDepartmentName(delivery.locality().department().value());
        deliveryEntity.setManagerCode(delivery.manager().code().value());
        deliveryEntity.setManagerName(delivery.manager().name().value());
        deliveryEntity.setMunicipalityCode(delivery.locality().code().value());
        deliveryEntity.setMunicipalityName(delivery.locality().municipality().value());
        deliveryEntity.setObservations(delivery.observations().value());
        deliveryEntity.setStatus(mappingEnum(delivery.status()));
        deliveryEntity.setUserCode(delivery.user().value());

        deliveryJPARepository.save(deliveryEntity);
    }

    private DeliveryStatusEnum mappingEnum(DeliveryStatus status) {
        switch (status.value()) {
            case SENT_CADASTRAL_AUTHORITY:
                return DeliveryStatusEnum.SENT_CADASTRAL_AUTHORITY;
            case IN_QUEUE_TO_IMPORT:
                return DeliveryStatusEnum.IN_QUEUE_TO_IMPORT;
            case IMPORTING:
                return DeliveryStatusEnum.IMPORTING;
            case SUCCESS_IMPORT:
                return DeliveryStatusEnum.SUCCESS_IMPORT;
            case FAILED_IMPORT:
                return DeliveryStatusEnum.FAILED_IMPORT;
            case DRAFT:
            default:
                return DeliveryStatusEnum.DRAFT;
        }
    }

}
