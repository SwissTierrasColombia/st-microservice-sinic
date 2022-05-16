package com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities;

public enum DeliveryStatusEnum {

    DRAFT("BORRADOR"), SENT_CADASTRAL_AUTHORITY("ENVIADO AUTORIDAD CATASTRAL"),
    IN_QUEUE_TO_IMPORT("ENVIADO AUTORIDAD CATASTRAL"), IMPORTING("ENVIADO AUTORIDAD CATASTRAL"),
    SUCCESS_IMPORT("MIGRADO A BASE DE DATOS"), FAILED_IMPORT("MIGRADO A BASE DE DATOS");

    private String status;

    DeliveryStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
