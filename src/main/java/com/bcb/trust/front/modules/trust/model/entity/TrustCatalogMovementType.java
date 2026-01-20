package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDateTime;

import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_catalog_movement_types")
public class TrustCatalogMovementType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movementTypeId;

    private String name;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer balanceEffect;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer inventoryEffect;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private SystemUserEntity createdBy;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    public TrustCatalogMovementType() {
    }

    public Long getMovementTypeId() {
        return movementTypeId;
    }

    public void setMovementTypeId(Long movementTypeId) {
        this.movementTypeId = movementTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBalanceEffect() {
        return balanceEffect;
    }

    public void setBalanceEffect(Integer balanceEffect) {
        this.balanceEffect = balanceEffect;
    }

    public Integer getInventoryEffect() {
        return inventoryEffect;
    }

    public void setInventoryEffect(Integer inventoryEffect) {
        this.inventoryEffect = inventoryEffect;
    }

    public SystemUserEntity getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(SystemUserEntity createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
