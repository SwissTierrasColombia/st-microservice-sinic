package com.ai.st.microservice.sinic.modules.files.domain;

import com.ai.st.microservice.sinic.modules.files.domain.exceptions.FileStatusInvalid;

public final class FileStatus {

    private final Status value;

    public enum Status {
        IN_VALIDATION,
        SUCCESSFUL,
        UNSUCCESSFUL
    }

    public FileStatus(Status value) {
        ensureStatus(value);
        this.value = value;
    }

    public static FileStatus fromValue(String value) {
        switch (value) {
            case "IN_VALIDATION":
                return new FileStatus(Status.IN_VALIDATION);
            case "SUCCESSFUL":
                return new FileStatus(Status.SUCCESSFUL);
            case "UNSUCCESSFUL":
                return new FileStatus(Status.UNSUCCESSFUL);
            default:
                throw new FileStatusInvalid(value);
        }
    }

    private void ensureStatus(Status value) {
        if (value == null) throw new FileStatusInvalid("N/A");
    }

    public Status value() {
        return value;
    }

}
