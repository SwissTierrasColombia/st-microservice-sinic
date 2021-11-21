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

    private void ensureStatus(Status value) {
        if (value == null) throw new DeliveryStatusInvalid("N/A");
    }

    public Status value() {
        return value;
    }

}
