package com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class UnauthorizedToCreateDelivery extends DomainError {

    public UnauthorizedToCreateDelivery(String message) {
        super("unauthorized_to_create_delivery", "Error creando la entrega: " + message);
    }
}
