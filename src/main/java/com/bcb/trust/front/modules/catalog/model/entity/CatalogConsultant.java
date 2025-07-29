package com.bcb.trust.front.modules.catalog.model.entity;

import java.time.LocalDateTime;

import com.bcb.trust.front.model.trusts.entity.catalog.Person;
import com.bcb.trust.front.model.trusts.enums.StatusEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "catalog_consultants")
public class CatalogConsultant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consultantId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PersonId", referencedColumnName = "PersonId")
    private Person person;

    private StatusEnum status;

    private LocalDateTime created;

    public CatalogConsultant() {
    }

    public Long getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(Long consultantId) {
        this.consultantId = consultantId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
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
        return "Consultant [consultantId=" + consultantId + ", person=" + person + ", status=" + status + ", created="
                + created + "]";
    }

}
