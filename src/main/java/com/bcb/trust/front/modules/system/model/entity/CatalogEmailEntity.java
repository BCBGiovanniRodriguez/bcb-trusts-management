package com.bcb.trust.front.modules.system.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_catalog_emails")
public class CatalogEmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailId;

    private String email;

    private Integer own;

    private LocalDateTime createdAt;

    public static final Integer EMAIL_TYPE_WORK = 1;

    public static final Integer EMAIL_TYPE_PERSON = 2;

    public static final String[] owns = {"Indefinido", "Personal", "Trabajo"};

    public CatalogEmailEntity() {
    }

    public Long getEmailId() {
        return emailId;
    }

    public void setEmailId(Long emailId) {
        this.emailId = emailId;
    }

    public Integer getOwn() {
        return own;
    }

    public void setOwn(Integer own) {
        this.own = own;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
    }

    @Override
    public String toString() {
        return "CatalogEmailEntity [emailId=" + emailId + ", type=" + own + ", email=" + email 
                + ", created=" + createdAt + "]";
    }

    public String getOwnAsString() throws Exception {
        if (this.own < 0 || (this.own > CatalogEmailEntity.owns.length)) {
            throw new Exception("CatalogEmailEntity::getOwnAsString::Valor de tipo fuera del rango");
        }

        return CatalogEmailEntity.owns[this.own];
    }
    
}
