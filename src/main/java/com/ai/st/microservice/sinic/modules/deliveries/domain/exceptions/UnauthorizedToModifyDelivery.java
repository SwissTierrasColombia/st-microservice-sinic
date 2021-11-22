package com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class UnauthorizedToModifyDelivery extends DomainError {

    public UnauthorizedToModifyDelivery(String message) {
        super("unauthorized_to_modify_delivery", message);
    }

}
