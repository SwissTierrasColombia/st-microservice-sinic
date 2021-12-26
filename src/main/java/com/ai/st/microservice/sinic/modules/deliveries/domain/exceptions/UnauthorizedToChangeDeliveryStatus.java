package com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class UnauthorizedToChangeDeliveryStatus extends DomainError {

    public UnauthorizedToChangeDeliveryStatus(String errorMessage) {
        super("unauthorized_to_change_delivery_status", errorMessage);
    }
}
