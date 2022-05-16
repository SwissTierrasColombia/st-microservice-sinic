package com.ai.st.microservice.sinic.modules.files.application;

import com.ai.st.microservice.sinic.modules.files.domain.File;
import com.ai.st.microservice.sinic.modules.shared.application.Response;

import java.util.Date;

public final class FileResponse implements Response {

    private final Long id;
    private final Date date;
    private final Date dateStatus;
    private final Boolean isValid;
    private final boolean hasLog;
    private final String observations;
    private final String status;
    private final String version;
    private final Long deliveryId;

    public FileResponse(Long id, Date date, Date dateStatus, Boolean isValid, boolean hasLog, String observations,
            String status, String version, Long deliveryId) {
        this.id = id;
        this.date = date;
        this.dateStatus = dateStatus;
        this.isValid = isValid;
        this.hasLog = hasLog;
        this.observations = observations;
        this.status = status;
        this.version = version;
        this.deliveryId = deliveryId;
    }

    public static FileResponse fromAggregate(File file) {
        return new FileResponse(file.id().value(), file.date().value(), file.dateStatus().value(), file.valid().value(),
                file.hasLog(), file.observations().value(), file.status().value().name(), file.version().value(),
                file.deliveryId().value());
    }

    public Long id() {
        return id;
    }

    public Date date() {
        return date;
    }

    public Date dateStatus() {
        return dateStatus;
    }

    public Boolean valid() {
        return isValid;
    }

    public boolean hasLog() {
        return hasLog;
    }

    public String observations() {
        return observations;
    }

    public String status() {
        return status;
    }

    public String version() {
        return version;
    }

    public Long deliveryId() {
        return deliveryId;
    }
}
