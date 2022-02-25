package com.ai.st.microservice.sinic.modules.deliveries.application;

import com.ai.st.microservice.sinic.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.sinic.modules.shared.application.Response;

import java.util.Date;

public final class DeliveryResponse implements Response {

    private final Long id;
    private final String code;
    private final Date date;
    private final Date dateStatus;
    private final Locality locality;
    private final Manager manager;
    private final String observations;
    private final String status;
    private final String type;

    public DeliveryResponse(Long id, String code, Date date, Date dateStatus, Locality locality, Manager manager,
                            String observations, String status, String type) {
        this.id = id;
        this.code = code;
        this.date = date;
        this.dateStatus = dateStatus;
        this.locality = locality;
        this.manager = manager;
        this.observations = observations;
        this.status = status;
        this.type = type;
    }

    public static DeliveryResponse fromAggregate(Delivery delivery) {
        return new DeliveryResponse(
                delivery.id().value(),
                delivery.code().value(),
                delivery.date().value(),
                delivery.dateStatus().value(),
                new Locality(delivery.locality().department().value(), delivery.locality().municipality().value(), delivery.locality().code().value()),
                new Manager(delivery.manager().name().value(), delivery.manager().code().value()),
                delivery.observations().value(),
                delivery.status().value().name(),
                delivery.type().value().name());
    }

    public Long id() {
        return id;
    }

    public String code() {
        return code;
    }

    public Date date() {
        return date;
    }

    public Date dateStatus() {
        return dateStatus;
    }

    public Locality locality() {
        return locality;
    }

    public Manager manager() {
        return manager;
    }

    public String observations() {
        return observations;
    }

    public String status() {
        return status;
    }

    public String type() {
        return type;
    }

    private static class Locality {

        private final String department;
        private final String municipality;
        private final String code;

        public Locality(String department, String municipality, String code) {
            this.department = department;
            this.municipality = municipality;
            this.code = code;
        }

        public String department() {
            return department;
        }

        public String municipality() {
            return municipality;
        }

        public String code() {
            return code;
        }

    }

    private static class Manager {

        private final String name;
        private final Long code;

        public Manager(String name, Long code) {
            this.name = name;
            this.code = code;
        }

        public String name() {
            return name;
        }

        public Long code() {
            return code;
        }
    }

}
