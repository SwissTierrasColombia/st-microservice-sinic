package com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities;

public enum FileStatusEnum {

    IN_VALIDATION("EN_VALIDACIÓN"),
    SUCCESSFUL("EXITOSO"),
    UNSUCCESSFUL("FALLIDO"),
    IMPORTING("IMPORTANDO"),
    IMPORT_SUCCESSFUL("IMPORTACIÓN_EXITOSA"),
    IMPORT_UNSUCCESSFUL("IMPORTACIÓN_FALLIDA");

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
