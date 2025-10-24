package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
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
    private Long trustWorkerId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trustId", referencedColumnName = "trustId")
    private TrustTrustEntity trustEntity;

    private Integer number;

    private Integer account;

    private String name;

    private Integer status;

    private LocalDate registerDate;

    private LocalDate endDate;

    private LocalDateTime created;

    public TrustTrustWorkerEntity() {
    }

    public Long getTrustWorkerId() {
        return trustWorkerId;
    }

    public void setTrustWorkerId(Long trustWorkerId) {
        this.trustWorkerId = trustWorkerId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    
    
    
}
