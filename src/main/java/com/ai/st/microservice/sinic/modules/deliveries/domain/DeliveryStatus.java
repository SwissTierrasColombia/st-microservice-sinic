package com.ai.st.microservice.sinic.modules.deliveries.domain;

import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.DeliveryStatusInvalid;

public final class DeliveryStatus {

    private final Status value;

    public enum Status {
        DRAFT,
        SENT_CADASTRAL_AUTHORITY,
        IN_QUEUE_TO_IMPORT,
        IMPORTING,
        SUCCESS_IMPORT,
        FAILED_IMPORT,
    }

    public DeliveryStatus(Status value) {
        ensureStatus(value);
        this.value = value;
    }

    public static DeliveryStatus fromValue(String value) {
        switch (value) {
            case "DRAFT":
                return new DeliveryStatus(Status.DRAFT);
            case "SENT_CADASTRAL_AUTHORITY":
                return new DeliveryStatus(Status.SENT_CADASTRAL_AUTHORITY);
            case "IN_QUEUE_TO_IMPORT":
                return new DeliveryStatus(Status.IN_QUEUE_TO_IMPORT);
            case "IMPORTING":
                return new DeliveryStatus(Status.IMPORTING);
            case "SUCCESS_IMPORT":
                return new DeliveryStatus(Status.SUCCESS_IMPORT);
            case "FAILED_IMPORT":
                return new DeliveryStatus(Status.FAILED_IMPORT);
            default:
                throw new DeliveryStatusInvalid(value);
        }
    }

    private void ensureStatus(Status value) {
        if (value == null) throw new DeliveryStatusInvalid("N/A");
    }

    public Status value() {
        return value;
    }

}
