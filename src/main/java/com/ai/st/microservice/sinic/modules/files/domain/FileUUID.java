package com.ai.st.microservice.sinic.modules.files.domain;

import java.util.UUID;

public final class FileUUID {

    private final String value;

    public FileUUID(String value) {
        ensureUUID(value);
        this.value = value;
    }

    public String value() {
        return value;
    }

    private void ensureUUID(String value) throws IllegalArgumentException {
        UUID.fromString(value);
    }

}
