package com.ai.st.microservice.sinic.modules.shared.infrastructure.persistence.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "deliveries", schema = "sinic")
public final class DeliveryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "code", nullable = false, length = 20, unique = true)
    private String code;

    @Column(name = "municipality_name", nullable = false)
    private String municipalityName;

    @Column(name = "department_name", nullable = false)
    private String departmentName;

    @Column(name = "municipality_code", nullable = false, length = 8)
    private String municipalityCode;

    @Column(name = "manager_name", nullable = false)
    private String managerName;

    @Column(name = "manager_code", nullable = false)
    private Long managerCode;

    @Column(name = "user_code", nullable = false)
    private Long userCode;

    @Column(name = "observations", nullable = false)
    private String observations;

    @Column(name = "status", nullable = false, length = 100)
    @Enumerated(value = EnumType.STRING)
    private DeliveryStatusEnum status;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "date_status_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStatusAt;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    private List<FileEntity> files = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getMunicipalityCode() {
        return municipalityCode;
    }

    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Long getManagerCode() {
        return managerCode;
    }

    public void setManagerCode(Long managerCode) {
        this.managerCode = managerCode;
    }

    public Long getUserCode() {
        return userCode;
    }

    public void setUserCode(Long userCode) {
        this.userCode = userCode;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public DeliveryStatusEnum getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatusEnum status) {
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

    public List<FileEntity> getFiles() {
        return files;
    }

    public void setFiles(List<FileEntity> files) {
        this.files = files;
    }
}
