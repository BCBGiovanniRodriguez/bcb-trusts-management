package com.bcb.trust.front.model.trusts.entity.trust;

import java.time.LocalDateTime;

import com.bcb.trust.front.model.trusts.entity.catalog.CatalogPerson;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "TrustTrusters")
public class TrustTrusters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long TrusterId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PersonId", referencedColumnName = "PersonId")
    private CatalogPerson Person;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TrustId", referencedColumnName = "TrustId")
    private TrustTrust Trust;

    private Integer Status;

    private LocalDateTime Created;

    public TrustTrusters() {
    }

    public Long getTrusterId() {
        return TrusterId;
    }

    public void setTrusterId(Long trusterId) {
        TrusterId = trusterId;
    }

    public CatalogPerson getPerson() {
        return Person;
    }

    public void setPerson(CatalogPerson person) {
        Person = person;
    }

    public TrustTrust getTrust() {
        return Trust;
    }

    public void setTrust(TrustTrust trust) {
        Trust = trust;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public LocalDateTime getCreated() {
        return Created;
    }

    public void setCreated(LocalDateTime created) {
        Created = created;
    }

    @Override
    public String toString() {
        return "Trusters [TrusterId=" + TrusterId + ", Person=" + Person + ", Trust=" + Trust + ", Status=" + Status
                + ", Created=" + Created + "]";
    }
    
}
