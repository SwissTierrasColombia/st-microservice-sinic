package com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class DeliveryDateStatusInvalid extends DomainError {

    public DeliveryDateStatusInvalid(String date) {
        super("delivery_date_status_invalid", String.format("La fecha del estado '%s' es inválida", date));
    }

}
