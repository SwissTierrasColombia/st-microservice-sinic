package com.ai.st.microservice.sinic.modules.files.application.find_files;

import com.ai.st.microservice.sinic.modules.shared.application.Query;
import com.ai.st.microservice.sinic.modules.shared.application.Roles;

public final class FilesFinderQuery implements Query {

    private final Long deliveryId;
    private final Roles role;
    private final Long managerCode;

    public FilesFinderQuery(Long deliveryId, Roles role, Long managerCode) {
        this.deliveryId = deliveryId;
        this.role = role;
        this.managerCode = managerCode;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Roles role() {
        return role;
    }

    public Long managerCode() {
        return managerCode;
    }
}
