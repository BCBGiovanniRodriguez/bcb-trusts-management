package com.bcb.trust.front.modules.catalog.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "catalog_phones")
public class CatalogPhoneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long phoneId;

    private Integer type;

    private Integer number;

    private Integer status;

    private LocalDateTime created;

    public static final Integer PHONE_TYPE_HOME = 1;

    public static final Integer PHONE_TYPE_WORK = 2;

    public static final Integer PHONE_TYPE_PERSON = 3;

    public static final String[] types = {"Casa", "Trabajo", "Personal"};

    public CatalogPhoneEntity() {
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
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
        return "CatalogPhoneEntity [phoneId=" + phoneId + ", type=" + type + ", number=" + number + ", status=" + status
                + ", created=" + created + "]";
    }

    public String getTypeAsString() throws Exception {
        if (this.type < 0 || (this.type > CatalogPhoneEntity.types.length)) {
            throw new Exception("CatalogPhoneEntity::getStatusAsString::Valor de tipo fuera del rango");
        }

        return CatalogPhoneEntity.types[this.type];
    }
        
}
