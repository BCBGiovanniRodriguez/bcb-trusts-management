package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDateTime;

import com.bcb.trust.front.modules.catalog.model.entity.CatalogPersonEntity;
import com.bcb.trust.front.modules.request.model.entity.RequestRequestEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_trustees")
public class TrustTrusteeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trusteeId;

    @ManyToOne
    @JoinColumn(name = "personId", nullable = false)
    private CatalogPersonEntity personEntity;

    @ManyToOne
    @JoinColumn(name = "trustId", nullable = true)
    private TrustTrustEntity trustEntity;

    private Integer status;

    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "requestId", nullable = false)
    private RequestRequestEntity request;

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

    public RequestRequestEntity getRequest() {
        return request;
    }

    public void setRequest(RequestRequestEntity request) {
        this.request = request;
    }

    
    
}
