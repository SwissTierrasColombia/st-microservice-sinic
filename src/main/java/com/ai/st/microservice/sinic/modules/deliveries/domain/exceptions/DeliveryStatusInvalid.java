package com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class DeliveryStatusInvalid extends DomainError {

    public DeliveryStatusInvalid(String status) {
        super("delivery_status_invalid", String.format("El estado de la entrega '%s' no es válido.", status));
    }

}
