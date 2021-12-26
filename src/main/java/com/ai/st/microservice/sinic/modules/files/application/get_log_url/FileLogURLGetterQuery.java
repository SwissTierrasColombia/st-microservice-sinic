package com.ai.st.microservice.sinic.modules.files.application.get_log_url;

import com.ai.st.microservice.sinic.modules.shared.application.Query;

public final class FileLogURLGetterQuery implements Query {

    private final Long deliveryId;
    private final Long fileId;
    private final Long managerCode;

    public FileLogURLGetterQuery(Long deliveryId, Long fileId, Long managerCode) {
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
