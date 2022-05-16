package com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities;

public enum DeliveryTypeEnum {

    XTF("ARCHIVOS XTF"), FLAT("ARCHIVOS PLANOS");

    private String type;

    DeliveryTypeEnum(String status) {
        this.type = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
