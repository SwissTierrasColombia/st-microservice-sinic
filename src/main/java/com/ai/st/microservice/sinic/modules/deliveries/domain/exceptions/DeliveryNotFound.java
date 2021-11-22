package com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class DeliveryNotFound extends DomainError {

    public DeliveryNotFound(Long deliveryId) {
        super("delivery_not_found", String.format("No se ha encontrado la entrega '%d'.", deliveryId));
    }
}
