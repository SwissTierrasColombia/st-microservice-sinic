package com.ai.st.microservice.sinic.modules.files.domain;

import com.ai.st.microservice.sinic.modules.files.domain.exceptions.FileDateInvalid;

import java.util.Date;

public final class FileDate {

    private final Date value;

    public FileDate(Date value) {
        ensureFormat(value);
        this.value = value;
    }

    private void ensureFormat(Date value) {
        if (value == null) {
            throw new FileDateInvalid("N/A");
        }
    }

    public Date value() {
        return value;
    }

}
