package com.ai.st.microservice.sinic.modules.files.application.get_file_url;

import com.ai.st.microservice.sinic.modules.shared.application.Query;
import com.ai.st.microservice.sinic.modules.shared.application.Roles;

public final class FileURLGetterQuery implements Query {

    private final Long deliveryId;
    private final Long fileId;
    private final Roles role;
    private final Long managerCode;

    public FileURLGetterQuery(Long deliveryId, Long fileId, Roles role, Long managerCode) {
        this.deliveryId = deliveryId;
        this.fileId = fileId;
        this.role = role;
        this.managerCode = managerCode;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Long fileId() {
        return fileId;
    }

    public Roles role() {
        return role;
    }

    public Long managerCode() {
        return managerCode;
    }
}
