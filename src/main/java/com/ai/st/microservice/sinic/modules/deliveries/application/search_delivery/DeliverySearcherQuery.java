package com.ai.st.microservice.sinic.modules.deliveries.application.search_delivery;

import com.ai.st.microservice.sinic.modules.shared.application.Query;
import com.ai.st.microservice.sinic.modules.shared.application.Roles;

public final class DeliverySearcherQuery implements Query {

    private final Long deliveryId;
    private final Roles role;
    private final Long entityCode;

    public DeliverySearcherQuery(Long deliveryId, Roles role, Long entityCode) {
        this.deliveryId = deliveryId;
        this.role = role;
        this.entityCode = entityCode;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Roles role() {
        return role;
    }

    public Long entityCode() {
        return entityCode;
    }

}
