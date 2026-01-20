package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDateTime;

import com.bcb.trust.front.modules.common.model.CommonEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_worker_movement_types")
public class TrustWorkerMovementType extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workerMovementTypeId;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean balanceEffect;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer status;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "trustId", nullable = false)
    private TrustTrustEntity trustEntity;

    public TrustWorkerMovementType() {
    }

    public Long getWorkerMovementTypeId() {
        return workerMovementTypeId;
    }

    public void setWorkerMovementTypeId(Long workerMovementTypeId) {
        this.workerMovementTypeId = workerMovementTypeId;
    }

    public Boolean getBalanceEffect() {
        return balanceEffect;
    }

    public void setBalanceEffect(Boolean affectBalance) {
        this.balanceEffect = affectBalance;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
    }

    @Override
    public String getStatusAsString() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStatusAsString'");
    }

}
