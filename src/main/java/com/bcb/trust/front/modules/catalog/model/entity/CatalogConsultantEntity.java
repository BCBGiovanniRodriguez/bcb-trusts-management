package com.bcb.trust.front.modules.catalog.model.entity;

import java.time.LocalDateTime;

import com.bcb.trust.front.model.trusts.enums.StatusEnum;
import com.bcb.trust.front.modules.system.model.entity.CatalogPersonEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin_catalog_consultants")
public class CatalogConsultantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consultantId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PersonId", referencedColumnName = "PersonId")
    private CatalogPersonEntity person;

    @Column(columnDefinition = "TINYINT(1)")
    private StatusEnum status;

    private LocalDateTime createdAt;

    public CatalogConsultantEntity() {
    }

    public Long getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(Long consultantId) {
        this.consultantId = consultantId;
    }

    public CatalogPersonEntity getPerson() {
        return person;
    }

    public void setPerson(CatalogPersonEntity person) {
        this.person = person;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
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
        return "Consultant [consultantId=" + consultantId + ", person=" + person + ", status=" + status + ", created="
                + createdAt + "]";
    }

}
