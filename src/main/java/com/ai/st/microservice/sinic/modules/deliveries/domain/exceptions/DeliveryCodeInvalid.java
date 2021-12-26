package com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class DeliveryCodeInvalid extends DomainError {

    public DeliveryCodeInvalid(String deliveryCode) {
        super("delivery_code_invalid", String.format("El código de la entrega '%s' es inválido.", deliveryCode));
    }

}
