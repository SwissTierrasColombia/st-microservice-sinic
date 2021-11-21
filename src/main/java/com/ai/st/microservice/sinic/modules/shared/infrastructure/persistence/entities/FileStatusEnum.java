package com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities;

public enum FileStatusEnum {

    IN_VALIDATION("EN_VALIDACIÓN"),
    SUCCESSFUL("EXITOSO"),
    UNSUCCESSFUL("FALLIDO");

    private String status;

    FileStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
