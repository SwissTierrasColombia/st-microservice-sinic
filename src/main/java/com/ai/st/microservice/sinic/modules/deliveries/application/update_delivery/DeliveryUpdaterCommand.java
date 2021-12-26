package com.ai.st.microservice.sinic.modules.deliveries.application.update_delivery;

import com.ai.st.microservice.sinic.modules.shared.application.Command;

public final class DeliveryUpdaterCommand implements Command {

    private final Long deliveryId;
    private final String observations;
    private final Long managerCode;

    public DeliveryUpdaterCommand(Long deliveryId, String observations, Long managerCode) {
        this.deliveryId = deliveryId;
        this.observations = observations;
        this.managerCode = managerCode;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public String observations() {
        return observations;
    }

    public Long managerCode() {
        return managerCode;
    }

}
