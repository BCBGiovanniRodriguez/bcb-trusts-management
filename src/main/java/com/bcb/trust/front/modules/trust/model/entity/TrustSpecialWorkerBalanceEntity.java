package com.bcb.trust.front.modules.trust.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_special_worker_balance")
public class TrustSpecialWorkerBalanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long balanceId;

    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private TrustSpecialWorkerEntity workerEntity;

    @ManyToOne
    @JoinColumn(name = "period_id", nullable = false)
    private TrustSpecialPeriodEntity periodEntity;

    @Column(name = "worker_deposits", columnDefinition = "DECIMAL(19, 6)")
    private Double workerDeposits;

    @Column(name = "worker_interest", columnDefinition = "DECIMAL(19, 6)")
    private Double workerInterest;

    @Column(name = "township_deposits", columnDefinition = "DECIMAL(19, 6)")
    private Double townshipDeposits;

    @Column(name = "township_interest", columnDefinition = "DECIMAL(19, 6)")
    private Double townshipInterest;

    @Column(name = "negative_worker_interest", columnDefinition = "DECIMAL(19, 6)")
    private Double negativeWorkerInterest;

    @Column(name = "negative_township_interest", columnDefinition = "DECIMAL(19, 6)")
    private Double negativeTownshipInterest;

    @Column(name = "worker_withdraw", columnDefinition = "DECIMAL(19, 6)")
    private Double workerWithdraw;

    @Column(name = "township_withdraw", columnDefinition = "DECIMAL(19, 6)")
    private Double townshipWithdraw;

    @Column(name = "worker_transfer", columnDefinition = "DECIMAL(19, 6)")
    private Double workerTransfer;

    @Column(name = "township_transfer", columnDefinition = "DECIMAL(19, 6)")
    private Double townshipTransfer;

    @Column(name = "balance", columnDefinition = "DECIMAL(19, 6)")
    private Double balance;

    public TrustSpecialWorkerBalanceEntity() {
    }

    public Long getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(Long balanceId) {
        this.balanceId = balanceId;
    }

    public TrustSpecialWorkerEntity getWorkerEntity() {
        return workerEntity;
    }

    public void setWorkerEntity(TrustSpecialWorkerEntity workerEntity) {
        this.workerEntity = workerEntity;
    }

    public TrustSpecialPeriodEntity getPeriodEntity() {
        return periodEntity;
    }

    public void setPeriodEntity(TrustSpecialPeriodEntity periodEntity) {
        this.periodEntity = periodEntity;
    }

    public Double getWorkerDeposits() {
        return workerDeposits;
    }

    public void setWorkerDeposits(Double workerDeposits) {
        this.workerDeposits = workerDeposits;
    }

    public Double getWorkerInterest() {
        return workerInterest;
    }

    public void setWorkerInterest(Double workerInterest) {
        this.workerInterest = workerInterest;
    }

    public Double getTownshipDeposits() {
        return townshipDeposits;
    }

    public void setTownshipDeposits(Double townshipDeposits) {
        this.townshipDeposits = townshipDeposits;
    }

    public Double getTownshipInterest() {
        return townshipInterest;
    }

    public void setTownshipInterest(Double townshipInterest) {
        this.townshipInterest = townshipInterest;
    }

    public Double getNegativeWorkerInterest() {
        return negativeWorkerInterest;
    }

    public void setNegativeWorkerInterest(Double negativeWorkerInterest) {
        this.negativeWorkerInterest = negativeWorkerInterest;
    }

    public Double getNegativeTownshipInterest() {
        return negativeTownshipInterest;
    }

    public void setNegativeTownshipInterest(Double negativeTownshipInterest) {
        this.negativeTownshipInterest = negativeTownshipInterest;
    }

    public Double getWorkerWithdraw() {
        return workerWithdraw;
    }

    public void setWorkerWithdraw(Double workerWithdraw) {
        this.workerWithdraw = workerWithdraw;
    }

    public Double getTownshipWithdraw() {
        return townshipWithdraw;
    }

    public void setTownshipWithdraw(Double townshipWithdraw) {
        this.townshipWithdraw = townshipWithdraw;
    }

    public Double getWorkerTransfer() {
        return workerTransfer;
    }

    public void setWorkerTransfer(Double transferWorker) {
        this.workerTransfer = transferWorker;
    }

    public Double getTownshipTransfer() {
        return townshipTransfer;
    }

    public void setTownshipTransfer(Double transferTownship) {
        this.townshipTransfer = transferTownship;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "TrustSpecialWorkerBalanceEntity [balanceId=" + balanceId + ", workerEntity=" + workerEntity
                + ", periodEntity=" + periodEntity + ", workerDeposits=" + workerDeposits + ", workerInterest="
                + workerInterest + ", townshipDeposits=" + townshipDeposits + ", townshipInterest=" + townshipInterest
                + ", negativeWorkerInterest=" + negativeWorkerInterest + ", negativeTownshipInterest="
                + negativeTownshipInterest + ", workerWithdraw=" + workerWithdraw + ", townshipWithdraw="
                + townshipWithdraw + ", workerTransfer=" + workerTransfer + ", townshipTransfer=" + townshipTransfer
                + ", balance=" + balance + "]";
    }
}
