package com.ai.st.microservice.sinic.modules.deliveries.domain;

import com.ai.st.microservice.sinic.modules.shared.domain.AggregateRoot;
import com.ai.st.microservice.sinic.modules.shared.domain.UserCode;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.DateTime;

public final class Delivery extends AggregateRoot {

    private final DeliveryId id;
    private final DeliveryCode code;
    private final DeliveryDate date;
    private final DeliveryDateStatus dateStatus;
    private final DeliveryManager manager;
    private final DeliveryLocality locality;
    private final DeliveryObservations observations;
    private final DeliveryStatus status;
    private final UserCode user;

    public Delivery(DeliveryId id, DeliveryCode code, DeliveryDate date, DeliveryDateStatus dateStatus,
                    DeliveryManager manager, DeliveryLocality locality, DeliveryObservations observations,
                    DeliveryStatus status, UserCode user) {
        this.id = id;
        this.code = code;
        this.date = date;
        this.dateStatus = dateStatus;
        this.manager = manager;
        this.locality = locality;
        this.observations = observations;
        this.status = status;
        this.user = user;
    }

    public static Delivery create(DeliveryCode code, DeliveryManager manager, DeliveryLocality municipality,
                                  DeliveryObservations observations, UserCode user, DateTime dateTime) {
        return new Delivery(
                null,
                code,
                new DeliveryDate(dateTime.now()),
                new DeliveryDateStatus(dateTime.now()),
                manager,
                municipality,
                observations,
                new DeliveryStatus(DeliveryStatus.Status.DRAFT),
                user
        );
    }

    public DeliveryId id() {
        return id;
    }

    public DeliveryCode code() {
        return code;
    }

    public DeliveryDate date() {
        return date;
    }

    public DeliveryDateStatus dateStatus() {
        return dateStatus;
    }

    public DeliveryManager manager() {
        return manager;
    }

    public DeliveryLocality locality() {
        return locality;
    }

    public DeliveryObservations observations() {
        return observations;
    }

    public DeliveryStatus status() {
        return status;
    }

    public UserCode user() {
        return user;
    }

}
