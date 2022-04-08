package com.ai.st.microservice.sinic.modules.files.domain;

import com.ai.st.microservice.sinic.modules.files.domain.exceptions.FileUrlInvalid;

public final class FileUrl {

    private final String value;

    public FileUrl(String value) {
        ensureUrl(value);
        this.value = value;
    }

    public String value() {
        return value;
    }

    private void ensureUrl(String value) {
        if (value == null || value.isEmpty())
            throw new FileUrlInvalid(value);
    }

}
