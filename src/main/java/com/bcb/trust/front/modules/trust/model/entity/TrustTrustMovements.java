package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDateTime;

import com.bcb.trust.front.modules.sisbur.model.entity.SisburCatalogInstrument;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_trust_movements")
public class TrustTrustMovements {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long trustMovementId;

    @ManyToOne
    @JoinColumn(name = "trustId", nullable = false)
    private TrustTrustEntity trust;

    @ManyToOne
    @JoinColumn(name = "instrumentId", nullable = false)
    private SisburCatalogInstrument instrument;

    @Column(columnDefinition = "INT")
    private Integer securities;

    @Column(columnDefinition = "DECIMAL(15,2)")
    private Double totalAmount;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime movementDate;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    public TrustTrustMovements() {
    }

    public Long getTrustMovementId() {
        return trustMovementId;
    }

    public void setTrustMovementId(Long trustMovementId) {
        this.trustMovementId = trustMovementId;
    }

    public TrustTrustEntity getTrust() {
        return trust;
    }

    public void setTrust(TrustTrustEntity trust) {
        this.trust = trust;
    }

    public SisburCatalogInstrument getInstrument() {
        return instrument;
    }

    public void setInstrument(SisburCatalogInstrument instrument) {
        this.instrument = instrument;
    }

    public Integer getSecurities() {
        return securities;
    }

    public void setSecurities(Integer securities) {
        this.securities = securities;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(LocalDateTime movementDate) {
        this.movementDate = movementDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
