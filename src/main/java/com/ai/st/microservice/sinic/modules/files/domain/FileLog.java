package com.ai.st.microservice.sinic.modules.files.domain;

public final class FileLog {

    private final String value;

    public FileLog(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static FileLog fromValue(String value) {
        return new FileLog(value);
    }

}
