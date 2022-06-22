package com.ai.st.microservice.sinic.modules.files.domain;

public final class FileSize {

    private final Long value;

    public FileSize(Long value) {
        this.value = value;
    }

    public Long value() {
        return value;
    }

}
