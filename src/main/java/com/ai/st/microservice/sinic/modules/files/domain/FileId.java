package com.ai.st.microservice.sinic.modules.files.domain;

public final class FileId {

    private final Long value;

    public FileId(Long value) {
        this.value = value;
    }

    public static FileId fromValue(Long value) {
        return new FileId(value);
    }

    public Long value() {
        return value;
    }

}
