package com.ai.st.microservice.sinic.modules.deliveries.application.send_delivery_to_cadastral_authority;

import com.ai.st.microservice.sinic.modules.shared.application.Command;

public final class DeliveryToCadastralAuthoritySenderCommand implements Command {

    private final Long deliveryId;
    private final Long managerCode;

    public DeliveryToCadastralAuthoritySenderCommand(Long deliveryId, Long managerCode) {
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
