package com.ai.st.microservice.sinic.modules.files.domain;

import com.ai.st.microservice.sinic.modules.files.domain.exceptions.FileDateStatusInvalid;

import java.util.Date;

public final class FileDateStatus {

    private final Date value;

    public FileDateStatus(Date value) {
        ensureFormat(value);
        this.value = value;
    }

    private void ensureFormat(Date value) {
        if (value == null) {
            throw new FileDateStatusInvalid("N/A");
        }
    }

    public Date value() {
        return value;
    }

}
