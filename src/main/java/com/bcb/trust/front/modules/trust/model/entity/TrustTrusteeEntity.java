package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDateTime;

import com.bcb.trust.front.modules.request.model.entity.RequestRequestEntity;
import com.bcb.trust.front.modules.system.model.entity.CatalogPersonEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_trust_trustees")
public class TrustTrusteeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trusteeId;

    @ManyToOne
    @JoinColumn(name = "trustId", nullable = true)
    private TrustTrustEntity trustEntity;

    @ManyToOne
    @JoinColumn(name = "personId", nullable = false)
    private CatalogPersonEntity personEntity;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer status;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
    }

    @Override
    public String toString() {
        return "TrustTrusteeEntity [trusteeId=" + trusteeId + ", personEntity=" + personEntity + ", trustEntity="
                + trustEntity + ", status=" + status + ", created=" + createdAt + "]";
    }

    public RequestRequestEntity getRequest() {
        return request;
    }

    public void setRequest(RequestRequestEntity request) {
        this.request = request;
    }

}
