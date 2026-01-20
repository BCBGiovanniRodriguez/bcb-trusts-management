package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_trust_worker_deparments")
public class TrustWorkerDepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    private String name;

    private String number;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "trust_id", nullable = false)
    private TrustTrustEntity trustEntity;

    public TrustWorkerDepartmentEntity() {
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
    }

    public TrustTrustEntity getTrustEntity() {
        return trustEntity;
    }

    public void setTrustEntity(TrustTrustEntity trust) {
        this.trustEntity = trust;
    }
}
