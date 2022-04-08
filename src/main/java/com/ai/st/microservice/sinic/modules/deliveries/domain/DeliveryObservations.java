package com.ai.st.microservice.sinic.modules.deliveries.domain;

import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.DeliveryObservationsInvalid;

public class DeliveryObservations {

    private final String value;

    public DeliveryObservations(String value) {
        ensureObservations(value);
        this.value = value;
    }

    public static DeliveryObservations fromValue(String value) {
        return new DeliveryObservations(value);
    }

    private void ensureObservations(String value) {
        if (value == null || value.isEmpty())
            throw new DeliveryObservationsInvalid(value);
    }

    public String value() {
        return value;
    }

}
