package com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.sinic.modules.shared.domain.DomainError;

public final class DeliveryObservationsInvalid extends DomainError {

    public DeliveryObservationsInvalid(String observations) {
        super("observations_invalid", String.format("Las observaciones '%s' no son válidas.", observations));
    }

}
