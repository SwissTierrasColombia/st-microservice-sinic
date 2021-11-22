package com.ai.st.microservice.sinic.modules.deliveries.domain;

import com.ai.st.microservice.sinic.modules.shared.domain.*;
import com.ai.st.microservice.sinic.modules.shared.domain.contracts.DateTime;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

    public static Delivery create(DeliveryId deliveryId, DeliveryCode code, DeliveryDate date, DeliveryDateStatus dateStatus,
                                  DeliveryManager manager, DeliveryLocality locality, DeliveryObservations observations, UserCode user) {
        return new Delivery(
                deliveryId,
                code,
                date,
                dateStatus,
                manager,
                locality,
                observations,
                new DeliveryStatus(DeliveryStatus.Status.DRAFT),
                user
        );
    }

    public static Delivery create(DeliveryCode code, DeliveryManager manager, DeliveryLocality locality,
                                  DeliveryObservations observations, UserCode user, DateTime dateTime) {
        return new Delivery(
                null,
                code,
                new DeliveryDate(dateTime.now()),
                new DeliveryDateStatus(dateTime.now()),
                manager,
                locality,
                observations,
                new DeliveryStatus(DeliveryStatus.Status.DRAFT),
                user
        );
    }

    public static Delivery fromPrimitives(Long id, String code, Date date, Date dateStatus, Long managerCode, String managerName,
                                          String departmentName, String municipalityName, String municipalityCode,
                                          String observations, String status, Long userCode) {


        DeliveryManager deliveryManager = DeliveryManager.builder()
                .name(ManagerName.fromValue(managerName)).code(ManagerCode.fromValue(managerCode)).build();

        DeliveryLocality deliveryLocality = DeliveryLocality.builder()
                .department(DepartmentName.fromValue(departmentName))
                .municipality(MunicipalityName.fromValue(municipalityName))
                .code(MunicipalityCode.fromValue(municipalityCode))
                .build();

        return new Delivery(
                DeliveryId.fromValue(id),
                DeliveryCode.fromValue(code),
                new DeliveryDate(date),
                new DeliveryDateStatus(dateStatus),
                deliveryManager,
                deliveryLocality,
                DeliveryObservations.fromValue(observations),
                DeliveryStatus.fromValue(status),
                UserCode.fromValue(userCode)
        );
    }

    public boolean deliveryBelongToManager(ManagerCode managerCode) {
        return managerCode.value().equals(manager.code().value());
    }

    public static List<DeliveryStatus> statusesAllowedToManager() {
        return Arrays.asList(
                new DeliveryStatus(DeliveryStatus.Status.DRAFT),
                new DeliveryStatus(DeliveryStatus.Status.SENT_CADASTRAL_AUTHORITY),
                new DeliveryStatus(DeliveryStatus.Status.IN_QUEUE_TO_IMPORT),
                new DeliveryStatus(DeliveryStatus.Status.IMPORTING),
                new DeliveryStatus(DeliveryStatus.Status.FAILED_IMPORT),
                new DeliveryStatus(DeliveryStatus.Status.SUCCESS_IMPORT)
        );
    }

    public static List<DeliveryStatus> statusesAllowedToCadastralAuthority() {
        return Arrays.asList(
                new DeliveryStatus(DeliveryStatus.Status.SENT_CADASTRAL_AUTHORITY),
                new DeliveryStatus(DeliveryStatus.Status.IN_QUEUE_TO_IMPORT),
                new DeliveryStatus(DeliveryStatus.Status.IMPORTING),
                new DeliveryStatus(DeliveryStatus.Status.FAILED_IMPORT),
                new DeliveryStatus(DeliveryStatus.Status.SUCCESS_IMPORT)
        );
    }

    public boolean isAvailableToCadastralAuthority() {
        DeliveryStatus statusFound =
                statusesAllowedToCadastralAuthority().stream().filter(s -> s.value().name().equals(status.value().name()))
                        .findAny().orElse(null);
        return statusFound != null;
    }

    public boolean isDraft() {
        return status.value().equals(DeliveryStatus.Status.DRAFT);
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
