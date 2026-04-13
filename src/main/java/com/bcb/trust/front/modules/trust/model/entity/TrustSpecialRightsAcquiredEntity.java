package com.bcb.trust.front.modules.trust.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_special_rights_acquired")
public class TrustSpecialRightsAcquiredEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rightAcquiredId;

    private Integer year;

    @Column(name = "percent", columnDefinition = "DECIMAL(4, 1)")
    private Double percent;

    public TrustSpecialRightsAcquiredEntity() {
    }

    public Long getRightAcquiredId() {
        return rightAcquiredId;
    }

    public void setRightAcquiredId(Long rightAcquiredId) {
        this.rightAcquiredId = rightAcquiredId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

}
