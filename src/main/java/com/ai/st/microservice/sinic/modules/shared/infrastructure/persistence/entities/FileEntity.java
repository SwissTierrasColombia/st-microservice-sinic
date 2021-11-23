package com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "files", schema = "sinic")
public final class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid", nullable = false, length = 36)
    private String uuid;

    @Column(name = "url", nullable = false, length = 1000)
    private String url;

    @Column(name = "log", length = 1000)
    private String log;

    @Column(name = "observations", length = 1000)
    private String observations;

    @Column(name = "version", length = 50)
    private String version;

    @Column(name = "is_valid")
    private Boolean isValid;

    @Column(name = "user_code", nullable = false)
    private Long userCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", referencedColumnName = "id", nullable = false)
    private DeliveryEntity delivery;

    @Column(name = "status", nullable = false, length = 100)
    @Enumerated(value = EnumType.STRING)
    private FileStatusEnum status;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "date_status_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStatusAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public Long getUserCode() {
        return userCode;
    }

    public void setUserCode(Long userCode) {
        this.userCode = userCode;
    }

    public FileStatusEnum getStatus() {
        return status;
    }

    public void setStatus(FileStatusEnum status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDateStatusAt() {
        return dateStatusAt;
    }

    public void setDateStatusAt(Date dateStatusAt) {
        this.dateStatusAt = dateStatusAt;
    }

    public DeliveryEntity getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryEntity delivery) {
        this.delivery = delivery;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
