package com.ai.st.microservice.sinic.modules.files.domain;

import com.ai.st.microservice.sinic.modules.files.domain.exceptions.FileStatusInvalid;

public final class FileStatus {

    private final Status value;

    public enum Status {
        IN_VALIDATION, SUCCESSFUL, UNSUCCESSFUL, IMPORTING, IMPORT_SUCCESSFUL, IMPORT_UNSUCCESSFUL
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
        case "IMPORTING":
            return new FileStatus(Status.IMPORTING);
        case "IMPORT_SUCCESSFUL":
            return new FileStatus(Status.IMPORT_SUCCESSFUL);
        case "IMPORT_UNSUCCESSFUL":
            return new FileStatus(Status.IMPORT_UNSUCCESSFUL);
        default:
            throw new FileStatusInvalid(value);
        }
    }

    private void ensureStatus(Status value) {
        if (value == null)
            throw new FileStatusInvalid("N/A");
    }

    public Status value() {
        return value;
    }

    public boolean importing() {
        return this.value.equals(Status.IMPORTING);
    }

    public boolean importSuccessful() {
        return this.value.equals(Status.IMPORT_SUCCESSFUL);
    }

    public boolean importUnsuccessful() {
        return this.value.equals(Status.IMPORT_UNSUCCESSFUL);
    }

}
