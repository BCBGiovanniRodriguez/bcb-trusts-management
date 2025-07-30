package com.bcb.trust.front.modules.catalog.model.entity;

import java.time.LocalDateTime;

import com.bcb.trust.front.model.trusts.enums.StatusEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "catalog_person_type")
public class CatalogPersonType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personTypeId;

    private String name;

    @Enumerated(EnumType.ORDINAL)
    private StatusEnum status;

    private LocalDateTime created;

    public CatalogPersonType() {
    }

    public Long getPersonTypeId() {
        return personTypeId;
    }

    public void setPersonTypeId(Long personTypeId) {
        this.personTypeId = personTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "PersonType [personTypeId=" + personTypeId + ", name=" + name + ", status=" + status + ", created="
                + created + "]";
    }
    
}
