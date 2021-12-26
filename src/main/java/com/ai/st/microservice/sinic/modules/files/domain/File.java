package com.ai.st.microservice.sinic.modules.files.domain;

import com.ai.st.microservice.sinic.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.sinic.modules.shared.domain.AggregateRoot;
import com.ai.st.microservice.sinic.modules.shared.domain.UserCode;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.DateTime;

import java.util.Date;

public final class File extends AggregateRoot {

    private final FileId id;
    private final FileUUID uuid;
    private final FileDate date;
    private final FileDateStatus dateStatus;
    private final FileValid valid;
    private final FileObservations observations;
    private final FileStatus status;
    private final FileUrl url;
    private final FileVersion version;
    private final FileLog log;
    private final UserCode user;
    private final DeliveryId deliveryId;

    public File(FileId id, FileUUID uuid, FileDate date, FileDateStatus dateStatus, FileValid valid, FileObservations observations,
                FileStatus status, FileUrl url, FileVersion version, FileLog log, UserCode user, DeliveryId deliveryId) {
        this.id = id;
        this.uuid = uuid;
        this.date = date;
        this.dateStatus = dateStatus;
        this.valid = valid;
        this.observations = observations;
        this.status = status;
        this.url = url;
        this.version = version;
        this.log = log;
        this.user = user;
        this.deliveryId = deliveryId;
    }

    public static File create(FileUUID uuid, FileObservations observations, FileUrl url, FileVersion version, UserCode user, DeliveryId deliveryId,
                              DateTime dateTime) {
        return new File(
                null,
                uuid,
                new FileDate(dateTime.now()),
                new FileDateStatus(dateTime.now()),
                null,
                observations,
                new FileStatus(FileStatus.Status.IN_VALIDATION),
                url,
                version,
                null,
                user,
                deliveryId
        );
    }

    public static File fromPrimitives(Long id, String uuid, Date date, Date dateStatus, Boolean valid, String observations,
                                      String status, String url, String version, String log, Long userCode, Long deliveryId) {
        return new File(
                FileId.fromValue(id),
                new FileUUID(uuid),
                new FileDate(date),
                new FileDateStatus(dateStatus),
                new FileValid(valid),
                FileObservations.fromValue(observations),
                FileStatus.fromValue(status),
                new FileUrl(url),
                new FileVersion(version),
                FileLog.fromValue(log),
                UserCode.fromValue(userCode),
                DeliveryId.fromValue(deliveryId)
        );
    }

    public boolean hasLog() {
        return log != null && log.value() != null;
    }

    public boolean allowToSendDelivery() {
        return status.value().equals(FileStatus.Status.SUCCESSFUL);
    }

    public boolean importing() {
        return status.value().equals(FileStatus.Status.IMPORTING);
    }

    public boolean importSuccessful() {
        return status.value().equals(FileStatus.Status.IMPORT_SUCCESSFUL);
    }

    public boolean importUnSuccessful() {
        return status.value().equals(FileStatus.Status.IMPORT_UNSUCCESSFUL);
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

    public FileUUID uuid() {
        return uuid;
    }

    public FileLog log() {
        return log;
    }
}
