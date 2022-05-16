package com.ai.st.microservice.sinic.modules.files.domain;

import com.ai.st.microservice.sinic.modules.files.domain.exceptions.FileVersionInvalid;

public final class FileVersion {

    private final String value;

    public FileVersion(String value) {
        ensureVersion(value);
        this.value = value;
    }

    private void ensureVersion(String value) {
        if (value == null || value.isEmpty())
            throw new FileVersionInvalid(value);
    }

    public String value() {
        return value;
    }

}
