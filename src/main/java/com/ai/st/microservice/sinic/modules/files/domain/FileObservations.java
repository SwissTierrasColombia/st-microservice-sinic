package com.ai.st.microservice.sinic.modules.files.domain;

import com.ai.st.microservice.sinic.modules.files.domain.exceptions.FileObservationsInvalid;

public class FileObservations {

    private final String value;

    public FileObservations(String value) {
        ensureObservations(value);
        this.value = value;
    }

    public static FileObservations fromValue(String value) {
        return new FileObservations(value);
    }

    private void ensureObservations(String value) {
        if (value == null || value.isEmpty())
            throw new FileObservationsInvalid(value);
    }

    public String value() {
        return value;
    }

}
