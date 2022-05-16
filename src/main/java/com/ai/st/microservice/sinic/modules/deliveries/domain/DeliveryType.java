package com.ai.st.microservice.sinic.modules.deliveries.domain;

import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.DeliveryTypeInvalid;

public final class DeliveryType {

    private final Type value;

    public enum Type {
        XTF, FLAT
    }

    public DeliveryType(Type value) {
        ensureType(value);
        this.value = value;
    }

    public static DeliveryType fromValue(String value) {
        switch (value) {
        case "XTF":
            return new DeliveryType(Type.XTF);
        case "FLAT":
            return new DeliveryType(Type.FLAT);
        default:
            throw new DeliveryTypeInvalid(value);
        }
    }

    private void ensureType(Type value) {
        if (value == null)
            throw new DeliveryTypeInvalid("N/A");
    }

    public Type value() {
        return value;
    }

}
