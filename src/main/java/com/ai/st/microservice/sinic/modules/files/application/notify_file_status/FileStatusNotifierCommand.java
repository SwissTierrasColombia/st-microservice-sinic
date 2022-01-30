package com.ai.st.microservice.sinic.modules.files.application.notify_file_status;

import com.ai.st.microservice.sinic.modules.shared.application.Command;

public class FileStatusNotifierCommand implements Command {

    public enum StatusFile {ACCEPTED, REJECTED}

    private final StatusFile status;
    private final String municipality;
    private final String department;
    private final Long userCode;

    public FileStatusNotifierCommand(StatusFile status, String municipality, String department, Long userCode) {
        this.status = status;
        this.municipality = municipality;
        this.department = department;
        this.userCode = userCode;
    }

    public StatusFile status() {
        return status;
    }

    public String municipality() {
        return municipality;
    }

    public String department() {
        return department;
    }

    public Long userCode() {
        return userCode;
    }
}
