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
@Table(name = "trust_trustors")
public class TrustTrustorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trustorId;

    @ManyToOne
    @JoinColumn(name = "personId", nullable = false)
    private CatalogPersonEntity person;

    @ManyToOne
    @JoinColumn(name = "requestId", nullable = false)
    private RequestRequestEntity request;

    @ManyToOne
    @JoinColumn(name = "trustId", nullable = true)
    private TrustTrustEntity trust;

    private Integer status;

    private LocalDateTime created;

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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "TrustTrustorEntity [trustorId=" + trustorId + ", person=" + person + ", Trust=" + trust + ", status="
                + status + ", created=" + created + "]";
    }

    public RequestRequestEntity getRequest() {
        return request;
    }

    public void setRequest(RequestRequestEntity request) {
        this.request = request;
    }
}
