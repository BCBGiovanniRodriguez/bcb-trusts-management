package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_quarters")
public class TrustQuarterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quarterId;

    private String name;

    private Integer year;

    private Integer fixed;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime created;

    public TrustQuarterEntity() {
    }

    public Long getQuarterId() {
        return quarterId;
    }

    public void setQuarterId(Long quarterId) {
        this.quarterId = quarterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getFixed() {
        return fixed;
    }

    public void setFixed(Integer fixed) {
        this.fixed = fixed;
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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    

}
