package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_worker_departments")
public class TrustWorkerDepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workerDepartmentId;

    private String name;

    private String number;

    private LocalDateTime created;

    public TrustWorkerDepartmentEntity() {
    }

    public Long getWorkerDepartmentId() {
        return workerDepartmentId;
    }

    public void setWorkerDepartmentId(Long workerDepartmentId) {
        this.workerDepartmentId = workerDepartmentId;
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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    
}
