package com.ai.st.microservice.sinic.modules.deliveries.application.notify_delivery_status;

import com.ai.st.microservice.sinic.modules.shared.application.Command;

public class DeliveryStatusNotifierCommand implements Command {

    public enum StatusDelivery {
        IMPORT_SUCCESSFUL
    }

    private final StatusDelivery status;
    private final String municipality;
    private final String department;
    private final Long managerCode;

    public DeliveryStatusNotifierCommand(StatusDelivery status, String municipality, String department,
            Long managerCode) {
        this.status = status;
        this.municipality = municipality;
        this.department = department;
        this.managerCode = managerCode;
    }

    public StatusDelivery status() {
        return status;
    }

    public String municipality() {
        return municipality;
    }

    public String department() {
        return department;
    }

    public Long managerCode() {
        return managerCode;
    }
}
