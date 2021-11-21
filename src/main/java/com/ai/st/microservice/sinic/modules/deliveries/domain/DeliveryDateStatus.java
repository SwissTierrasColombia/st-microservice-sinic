package com.ai.st.microservice.sinic.modules.deliveries.domain;

import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.DeliveryDateStatusInvalid;

import java.util.Date;

public final class DeliveryDateStatus {

    private final Date value;

    public DeliveryDateStatus(Date value) {
        ensureFormat(value);
        this.value = value;
    }

    private void ensureFormat(Date value) {
        if (value == null) {
            throw new DeliveryDateStatusInvalid("N/A");
        }
    }

    public Date value() {
        return value;
    }

}
