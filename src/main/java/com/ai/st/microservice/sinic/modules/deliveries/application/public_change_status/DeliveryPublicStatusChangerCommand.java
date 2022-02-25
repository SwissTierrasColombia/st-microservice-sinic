package com.ai.st.microservice.sinic.modules.deliveries.application.public_change_status;

import com.ai.st.microservice.sinic.modules.shared.application.Command;

public class DeliveryPublicStatusChangerCommand implements Command {

    private final Long deliveryId;
    private final Status status;

    public enum Status {
        SENT_CADASTRAL_AUTHORITY,
        IN_QUEUE_TO_IMPORT,
        IMPORTING,
        SUCCESS_IMPORT,
        FAILED_IMPORT,
    }

    public DeliveryPublicStatusChangerCommand(Long deliveryId, Status status) {
        this.deliveryId = deliveryId;
        this.status = status;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Status status() {
        return status;
    }

}
