package com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cycles", schema = "sinic")
public class CycleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "observations", nullable = false)
    private String observations;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "amount_periods", nullable = false)
    private Integer amountPeriods;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Transient
    private List<PeriodEntity> periods;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Integer getAmountPeriods() {
        return amountPeriods;
    }

    public void setAmountPeriods(Integer amountPeriods) {
        this.amountPeriods = amountPeriods;
    }

    public List<PeriodEntity> getPeriods() {
        return periods;
    }

    public void setPeriods(List<PeriodEntity> periods) {
        this.periods = periods;
    }
}
