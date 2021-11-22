package com.ai.st.microservice.sinic.modules.files.domain;

import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.sinic.modules.shared.domain.AggregateRoot;
import com.ai.st.microservice.sinic.modules.shared.domain.UserCode;

public final class File extends AggregateRoot {

    private final FileId id;
    private final FileDate date;
    private final FileDateStatus dateStatus;
    private final FileValid valid;
    private final FileObservations observations;
    private final FileStatus status;
    private final FileUrl url;
    private final FileVersion version;
    private final UserCode user;
    private final DeliveryId deliveryId;

    public File(FileId id, FileDate date, FileDateStatus dateStatus, FileValid valid, FileObservations observations,
                FileStatus status, FileUrl url, FileVersion version, UserCode user, DeliveryId deliveryId) {
        this.id = id;
        this.date = date;
        this.dateStatus = dateStatus;
        this.valid = valid;
        this.observations = observations;
        this.status = status;
        this.url = url;
        this.version = version;
        this.user = user;
        this.deliveryId = deliveryId;
    }

    public FileId id() {
        return id;
    }

    public FileDate date() {
        return date;
    }

    public FileDateStatus dateStatus() {
        return dateStatus;
    }

    public FileValid valid() {
        return valid;
    }

    public FileObservations observations() {
        return observations;
    }

    public FileStatus status() {
        return status;
    }

    public FileUrl url() {
        return url;
    }

    public FileVersion version() {
        return version;
    }

    public UserCode user() {
        return user;
    }

    public DeliveryId deliveryId() {
        return deliveryId;
    }
}
