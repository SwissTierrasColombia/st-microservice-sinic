package com.ai.st.microservice.sinic.modules.deliveries.application.remove_delivery;

import com.ai.st.microservice.sinic.modules.shared.application.Command;

public final class DeliveryRemoverCommand implements Command {

    private final Long deliveryId;
    private final Long managerCode;

    public DeliveryRemoverCommand(Long deliveryId, Long managerCode) {
        this.deliveryId = deliveryId;
        this.managerCode = managerCode;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Long managerCode() {
        return managerCode;
    }

}
