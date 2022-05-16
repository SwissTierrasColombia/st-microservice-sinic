package com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public class DeliveryTypeInvalid extends DomainError {

    public DeliveryTypeInvalid(String status) {
        super("delivery_type_invalid", String.format("El tipo de la entrega '%s' no es válido.", status));
    }

}
