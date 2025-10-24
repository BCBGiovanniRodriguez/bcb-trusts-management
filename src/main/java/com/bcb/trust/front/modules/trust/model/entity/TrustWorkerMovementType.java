package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDateTime;

import com.bcb.trust.front.modules.common.model.CommonEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_worker_movement_types")
public class TrustWorkerMovementType  extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workerMovementTypeId;

    private Boolean affectBalance;

    private Integer status;

    private LocalDateTime created;

    public TrustWorkerMovementType() {
    }

    public Long getWorkerMovementTypeId() {
        return workerMovementTypeId;
    }

    public void setWorkerMovementTypeId(Long workerMovementTypeId) {
        this.workerMovementTypeId = workerMovementTypeId;
    }

    public Boolean getAffectBalance() {
        return affectBalance;
    }

    public void setAffectBalance(Boolean affectBalance) {
        this.affectBalance = affectBalance;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public String getStatusAsString() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStatusAsString'");
    }

    

}
