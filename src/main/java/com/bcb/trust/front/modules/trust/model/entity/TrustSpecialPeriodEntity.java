package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_special_periods")
public class TrustSpecialPeriodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long periodId;

    private Integer trustNumber;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "trust_id", nullable = false)
    private TrustTrustEntity trustEntity;

    @ManyToOne
    @JoinColumn(name = "year_id", nullable = false)
    private TrustSpecialYearEntity yearEntity;

    @OneToMany(mappedBy = "periodEntity")
    private List<TrustSpecialWorkerBalanceEntity> balances;

    public TrustSpecialPeriodEntity() {
    }

    public Long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Long trustSpecialPeriodId) {
        this.periodId = trustSpecialPeriodId;
    }

    public Integer getTrustNumber() {
        return trustNumber;
    }

    public void setTrustNumber(Integer trustNumber) {
        this.trustNumber = trustNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate dateStart) {
        this.startDate = dateStart;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate dateEnd) {
        this.endDate = dateEnd;
    }
    
    public TrustTrustEntity getTrustEntity() {
        return trustEntity;
    }

    public void setTrustEntity(TrustTrustEntity trustEntity) {
        this.trustEntity = trustEntity;
    }

    public TrustSpecialYearEntity getYearEntity() {
        return yearEntity;
    }

    public void setYearEntity(TrustSpecialYearEntity yearEntity) {
        this.yearEntity = yearEntity;
    }
}
