package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDateTime;

import com.bcb.trust.front.modules.catalog.model.entity.CatalogPersonEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_trustees")
public class TrustTrusteeEntity {

    private Long trusteeId;

    private CatalogPersonEntity personEntity;

    private TrustTrustEntity trustEntity;

    private Integer status;

    private LocalDateTime created;

    public TrustTrusteeEntity() {
    }

    public Long getTrusteeId() {
        return trusteeId;
    }

    public void setTrusteeId(Long trusteeId) {
        this.trusteeId = trusteeId;
    }

    public CatalogPersonEntity getPersonEntity() {
        return personEntity;
    }

    public void setPersonEntity(CatalogPersonEntity personEntity) {
        this.personEntity = personEntity;
    }

    public TrustTrustEntity getTrustEntity() {
        return trustEntity;
    }

    public void setTrustEntity(TrustTrustEntity trustEntity) {
        this.trustEntity = trustEntity;
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
    public String toString() {
        return "TrustTrusteeEntity [trusteeId=" + trusteeId + ", personEntity=" + personEntity + ", trustEntity="
                + trustEntity + ", status=" + status + ", created=" + created + "]";
    }
    
}
