package com.bcb.trust.front.modules.catalog.model.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "catalog_emails")
public class CatalogEmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailId;

    private Integer type;

    private Integer email;

    private Integer status;

    private LocalDate created;

    public static final Integer EMAIL_TYPE_WORK = 1;

    public static final Integer EMAIL_TYPE_PERSON = 2;

    public static final String[] types = {"Trabajo", "Personal"};

    public CatalogEmailEntity() {
    }

    public Long getEmailId() {
        return emailId;
    }

    public void setEmailId(Long emailId) {
        this.emailId = emailId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getEmail() {
        return email;
    }

    public void setEmail(Integer email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "CatalogEmailEntity [emailId=" + emailId + ", type=" + type + ", email=" + email + ", status=" + status
                + ", created=" + created + "]";
    }

    public String getTypeAsString() throws Exception {
        if (this.type < 0 || (this.type > CatalogEmailEntity.types.length)) {
            throw new Exception("CatalogEmailEntity::getStatusAsString::Valor de tipo fuera del rango");
        }

        return CatalogEmailEntity.types[this.type];
    }
    
}
