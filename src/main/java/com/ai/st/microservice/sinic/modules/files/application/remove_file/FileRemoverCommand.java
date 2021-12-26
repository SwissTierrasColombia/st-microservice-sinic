package com.ai.st.microservice.sinic.modules.files.application.remove_file;

import com.ai.st.microservice.sinic.modules.shared.application.Command;

public final class FileRemoverCommand implements Command {

    private final Long deliveryId;
    private final Long fileId;
    private final Long managerCode;

    public FileRemoverCommand(Long deliveryId, Long fileId, Long managerCode) {
        this.deliveryId = deliveryId;
        this.fileId = fileId;
        this.managerCode = managerCode;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Long fileId() {
        return fileId;
    }

    public Long managerCode() {
        return managerCode;
    }
}
