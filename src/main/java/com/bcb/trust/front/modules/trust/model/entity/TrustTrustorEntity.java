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
@Table(name = "trust_trust_trustors")
public class TrustTrustorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trustorId;

    @ManyToOne
    @JoinColumn(name = "trustId", nullable = true)
    private TrustTrustEntity trust;

    @ManyToOne
    @JoinColumn(name = "requestId", nullable = false)
    private RequestRequestEntity request;

    @ManyToOne
    @JoinColumn(name = "personId", nullable = false)
    private CatalogPersonEntity person;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer status;

    private LocalDateTime createdAt;

    public TrustTrustorEntity() {
    }

    public Long getTrustorId() {
        return trustorId;
    }

    public void setTrustorId(Long trustorId) {
        this.trustorId = trustorId;
    }

    public CatalogPersonEntity getPerson() {
        return person;
    }

    public void setPerson(CatalogPersonEntity person) {
        this.person = person;
    }

    public TrustTrustEntity getTrust() {
        return trust;
    }

    public void setTrust(TrustTrustEntity trust) {
        this.trust = trust;
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
        return "TrustTrustorEntity [trustorId=" + trustorId + ", person=" + person + ", Trust=" + trust + ", status="
                + status + ", created=" + createdAt + "]";
    }

    public RequestRequestEntity getRequest() {
        return request;
    }

    public void setRequest(RequestRequestEntity request) {
        this.request = request;
    }
}
