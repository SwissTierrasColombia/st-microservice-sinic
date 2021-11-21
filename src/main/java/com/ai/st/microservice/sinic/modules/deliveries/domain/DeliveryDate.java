package com.ai.st.microservice.sinic.modules.deliveries.domain;

import com.ai.st.microservice.sinic.modules.deliveries.domain.exceptions.DeliveryDateInvalid;

import java.util.Date;

public final class DeliveryDate {

    private final Date value;

    public DeliveryDate(Date value) {
        ensureFormat(value);
        this.value = value;
    }

    private void ensureFormat(Date value) {
        if (value == null) {
            throw new DeliveryDateInvalid("N/A");
        }
    }

    public Date value() {
        return value;
    }

}
