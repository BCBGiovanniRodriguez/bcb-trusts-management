package com.bcb.trust.front.modules.catalog.model.entity;

import java.time.LocalDateTime;

import com.bcb.trust.front.modules.system.model.entity.CatalogAddressEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

//@Entity
//@Table(name = "admin_catalog_consultant_addresses")
public class CatalogConsultantAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consultantAddressId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "consultant_id")
    private CatalogConsultantEntity consultantEntity;

    @OneToOne
    @JoinColumn(name = "address_id")
    private CatalogAddressEntity addressEntity;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer type;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer status;

    private LocalDateTime createdAt;

    public CatalogConsultantAddressEntity() {
    }

    public Long getConsultantAddressId() {
        return consultantAddressId;
    }

    public void setConsultantAddressId(Long consultantAddressId) {
        this.consultantAddressId = consultantAddressId;
    }

    public CatalogConsultantEntity getConsultantEntity() {
        return consultantEntity;
    }

    public void setConsultantEntity(CatalogConsultantEntity consultantEntity) {
        this.consultantEntity = consultantEntity;
    }

    public CatalogAddressEntity getAddressEntity() {
        return addressEntity;
    }

    public void setAddressEntity(CatalogAddressEntity addressEntity) {
        this.addressEntity = addressEntity;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
        return "CatalogConsultantAddressEntity [consultantAddressId=" + consultantAddressId + ", consultantEntity="
                + consultantEntity + ", addressEntity=" + addressEntity + ", type=" + type + ", status=" + status
                + ", created=" + createdAt + "]";
    }
    
}
