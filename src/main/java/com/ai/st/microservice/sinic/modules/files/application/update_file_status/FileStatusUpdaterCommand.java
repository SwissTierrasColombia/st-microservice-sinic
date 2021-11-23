package com.ai.st.microservice.sinic.modules.files.application.update_file_status;

import com.ai.st.microservice.sinic.modules.shared.application.Command;

public final class FileStatusUpdaterCommand implements Command {

    public enum Status {SUCCESSFUL, UNSUCCESSFUL}

    private final Status status;
    private final String fileUUID;
    private final String log;

    public FileStatusUpdaterCommand(Status status, String fileUUID, String log) {
        this.status = status;
        this.fileUUID = fileUUID;
        this.log = log;
    }

    public Status status() {
        return status;
    }

    public String fileUUID() {
        return fileUUID;
    }

    public String log() {
        return log;
    }
}
