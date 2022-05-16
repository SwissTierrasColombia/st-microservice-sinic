package com.ai.st.microservice.sinic.modules.deliveries.domain;

import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.DeliveryCodeInvalid;

public final class DeliveryCode {

    private final String value;

    public DeliveryCode(String value) {
        ensureCode(value);
        this.value = value;
    }

    private void ensureCode(String value) {
        if (value == null || value.isEmpty())
            throw new DeliveryCodeInvalid(value);
    }

    public static DeliveryCode fromValue(String value) {
        return new DeliveryCode(value);
    }

    public String value() {
        return value;
    }

}
