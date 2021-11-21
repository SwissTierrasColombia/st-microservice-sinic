package com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class DeliveryDateInvalid extends DomainError {

    public DeliveryDateInvalid(String date) {
        super("delivery_date_invalid", String.format("La fecha de la entrega '%s' es inválida", date));
    }

}
