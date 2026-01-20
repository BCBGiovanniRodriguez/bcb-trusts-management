package com.bcb.trust.front.modules.system.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_catalog_phones")
public class CatalogPhoneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long phoneId;

    private Integer number;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer type;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer own;

    private LocalDateTime createdAt;

    public static final Integer PHONE_TYPE_FIXED = 1;

    public static final Integer PHONE_TYPE_MOBILE = 2;

    public static final String[] types = {"Indefinido" , "Fijo", "Móvil"};
    
    public static final Integer PHONE_OWN_SELF = 1;
    
    public static final Integer PHONE_OWN_WORK = 2;
    
    public static final String[] owns = {"Indefinido", "Personal", "Trabajo"};


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

    public Integer getOwn() {
        return own;
    }

    public void setOwn(Integer own) {
        this.own = own;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
    }

    @Override
    public String toString() {
        return "CatalogPhoneEntity [phoneId=" + phoneId + ", type=" + type + ", number=" + number + ", own=" + own
                + ", created=" + createdAt + "]";
    }

    public String getTypeAsString() throws Exception {
        if (this.type < 0 || (this.type > CatalogPhoneEntity.types.length)) {
            throw new Exception("CatalogPhoneEntity::getTypeAsString::Valor de tipo fuera del rango");
        }

        return CatalogPhoneEntity.types[this.type];
    }
    

    public String getOwnAsString() throws Exception {
        if (this.own < 0 || (this.own > CatalogPhoneEntity.owns.length)) {
            throw new Exception("CatalogPhoneEntity::getOwnAsString::Valor de pertenencia fuera del rango");
        }

        return CatalogPhoneEntity.owns[this.own];
    }

}
