package com.ai.st.microservice.sinic.modules.deliveries.application.create_delivery;

import com.ai.st.microservice.sinic.modules.shared.application.Command;

public final class CreateDeliveryCommand implements Command {

    public enum DeliveryType {
        XTF,
        FLAT
    }

    private final String municipalityCode;
    private final Long managerCode;
    private final Long userCode;
    private final String observations;
    private final DeliveryType type;


    public CreateDeliveryCommand(String municipalityCode, Long managerCode, Long userCode, String observations, DeliveryType type) {
        this.municipalityCode = municipalityCode;
        this.managerCode = managerCode;
        this.userCode = userCode;
        this.observations = observations;
        this.type = type;
    }

    public String municipalityCode() {
        return municipalityCode;
    }

    public Long managerCode() {
        return managerCode;
    }

    public Long userCode() {
        return userCode;
    }

    public String observations() {
        return observations;
    }

    public DeliveryType type() {
        return type;
    }
}
