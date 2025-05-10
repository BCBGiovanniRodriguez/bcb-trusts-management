package com.bcb.trust.front.model.bmtkfweb.dto;

public class PercentageRightsAcquired {

    private Integer year;

    private Double percentage;

    public PercentageRightsAcquired() { }

    public PercentageRightsAcquired(Integer year, Double percentage) {
        this.year = year;
        this.percentage = percentage;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "PercentageRightsAcquired [year=" + year + ", percentage=" + percentage + "]";
    }

}
