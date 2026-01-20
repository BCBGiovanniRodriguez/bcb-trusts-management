package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_trust_workers")
public class TrustTrustWorkerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workerId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trustId", referencedColumnName = "trustId")
    private TrustTrustEntity trustEntity;

    private Integer number;

    private String fullname;
    
    private Integer account;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer status;

    private LocalDate registerDate;

    private LocalDate endDate;

    private LocalDateTime createdAt;

    public TrustTrustWorkerEntity() {
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long trustWorkerId) {
        this.workerId = trustWorkerId;
    }

    public TrustTrustEntity getTrust() {
        return trustEntity;
    }

    public void setTrust(TrustTrustEntity trustEntity) {
        this.trustEntity = trustEntity;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String name) {
        this.fullname = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDate registerDate) {
        this.registerDate = registerDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
    }

    
    
    
}
