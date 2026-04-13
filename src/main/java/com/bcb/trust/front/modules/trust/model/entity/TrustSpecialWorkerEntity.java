package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_special_workers")
public class TrustSpecialWorkerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workerId;

    @ManyToOne
    @JoinColumn(name = "trust_id", nullable = false)
    private TrustTrustEntity trustEntity;

    private Integer number;

    private String account;

    private String name;

    @Column(columnDefinition = "TINYINT")
    private Integer status;
    
    private LocalDate startWorkDate;
    
    private LocalDate endWorkDate;
    
    private LocalDateTime createdAt;

    private String department;

    @OneToMany(mappedBy = "workerEntity", fetch = FetchType.EAGER)
    private List<TrustSpecialWorkerBalanceEntity> balances;    

    @OneToMany(mappedBy = "workerEntity")
    private List<TrustSpecialWorkerYearBalanceEntity> yearBalance;

    public TrustSpecialWorkerEntity() {
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long trustSpecialWorkerId) {
        this.workerId = trustSpecialWorkerId;
    }

    public TrustTrustEntity getTrustEntity() {
        return trustEntity;
    }

    public void setTrustEntity(TrustTrustEntity trust) {
        this.trustEntity = trust;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
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

    public LocalDate getStartWorkDate() {
        return startWorkDate;
    }

    public void setStartWorkDate(LocalDate dateStartWork) {
        this.startWorkDate = dateStartWork;
    }

    public LocalDate getEndWorkDate() {
        return endWorkDate;
    }

    public void setEndWorkDate(LocalDate dateEndWork) {
        this.endWorkDate = dateEndWork;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime dateCreated) {
        this.createdAt = dateCreated;
    }

    public List<TrustSpecialWorkerBalanceEntity> getBalances() {
        return balances;
    }

    public void setBalances(List<TrustSpecialWorkerBalanceEntity> balances) {
        this.balances = balances;
    }

    public List<TrustSpecialWorkerYearBalanceEntity> getYearBalance() {
        return yearBalance;
    }

    public void setYearBalance(List<TrustSpecialWorkerYearBalanceEntity> yearBalance) {
        this.yearBalance = yearBalance;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
