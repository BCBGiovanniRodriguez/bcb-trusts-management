package com.bcb.trust.front.modules.catalog.model.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.bcb.trust.front.modules.request.model.entity.RequestRequestEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "catalog_addresses")
public class CatalogAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    private String street;

    private String internalNumber;

    private String externalNumber;

    private String zipcode;

    private Long colonyId;

    private String fullAddress;

    private LocalDateTime created;

    @OneToMany(mappedBy = "addressEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RequestRequestEntity> requestSet;

    public CatalogAddressEntity() {
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getInternalNumber() {
        return internalNumber;
    }

    public void setInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
    }

    public String getExternalNumber() {
        return externalNumber;
    }

    public void setExternalNumber(String externalNumber) {
        this.externalNumber = externalNumber;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Long getColonyId() {
        return colonyId;
    }

    public void setColonyId(Long colonyId) {
        this.colonyId = colonyId;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "CatalogAddressEntity [addressId=" + addressId + ", street=" + street + ", internalNumber="
                + internalNumber + ", externalNumber=" + externalNumber + ", zipcode=" + zipcode + ", colonyId="
                + colonyId + ", fullAddress=" + fullAddress + ", created=" + created + "]";
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("addressId", this.addressId);
        map.put("street", this.street);
        map.put("internalNumber", this.internalNumber);
        map.put("externalNumber", this.externalNumber);
        map.put("zipcode", this.zipcode);
        map.put("colonyId", this.colonyId);
        map.put("fullAddress", this.fullAddress);
        map.put("created", this.created);

        return map;
    }
    
}
