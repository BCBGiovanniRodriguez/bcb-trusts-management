package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
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
@Table(name = "trust_special_years")
public class TrustSpecialYearEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long yearId;

    @ManyToOne
    @JoinColumn(name = "trust_id", nullable = false)
    private TrustTrustEntity trustEntity;

    private Integer year;

    private LocalDate startDate;

    private LocalDate endDate;

    @OneToMany(mappedBy = "yearEntity")
    private List<TrustSpecialWorkerYearBalanceEntity> yearBalance;

    @OneToMany(mappedBy = "yearEntity", orphanRemoval = true)
    private List<TrustSpecialPeriodEntity> specialPeriodList = new ArrayList<>();

    public TrustSpecialYearEntity() {
    }

    public Long getYearId() {
        return yearId;
    }

    public void setYearId(Long yearId) {
        this.yearId = yearId;
    }

    public TrustTrustEntity getTrustEntity() {
        return trustEntity;
    }

    public void setTrustEntity(TrustTrustEntity trustEntity) {
        this.trustEntity = trustEntity;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<TrustSpecialPeriodEntity> getSpecialPeriodList() {
        return specialPeriodList;
    }

    public void setSpecialPeriodList(List<TrustSpecialPeriodEntity> specialPeriodList) {
        this.specialPeriodList = specialPeriodList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((yearId == null) ? 0 : yearId.hashCode());
        result = prime * result + ((year == null) ? 0 : year.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TrustSpecialYearEntity other = (TrustSpecialYearEntity) obj;
        if (yearId == null) {
            if (other.yearId != null)
                return false;
        } else if (!yearId.equals(other.yearId))
            return false;
        if (year == null) {
            if (other.year != null)
                return false;
        } else if (!year.equals(other.year))
            return false;
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        } else if (!startDate.equals(other.startDate))
            return false;
        if (endDate == null) {
            if (other.endDate != null)
                return false;
        } else if (!endDate.equals(other.endDate))
            return false;
        return true;
    }

    
}
