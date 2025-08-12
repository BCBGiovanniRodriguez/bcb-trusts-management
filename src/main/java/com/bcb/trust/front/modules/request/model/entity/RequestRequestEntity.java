package com.bcb.trust.front.modules.request.model.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.bcb.trust.front.modules.catalog.model.entity.CatalogAddressEntity;
import com.bcb.trust.front.modules.catalog.model.entity.CatalogPersonEntity;
import com.bcb.trust.front.modules.common.model.CommonEntity;
import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustTypeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "request_requests")
public class RequestRequestEntity extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @Column(length = 20, nullable = false)
    private String number;

    private Integer trustChange;

    private String trustChangeTrust;

    private Integer wasRefered;

    private Integer wasReferedBy;

    private String wasReferedByFullName;

    @ManyToOne
    @JoinColumn(name = "trust_type_id", nullable = false)
    private TrustTrustTypeEntity trustTypeEntity;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private CatalogAddressEntity addressEntity;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private CatalogPersonEntity personEntity;

    private Integer state;

    private Integer status;
    
    private LocalDateTime created;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "registeredBy", referencedColumnName = "userId")
    private SystemUserEntity registeredBy;

    public RequestRequestEntity() {
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getTrustChange() {
        return trustChange;
    }

    public void setTrustChange(Integer trustChange) {
        this.trustChange = trustChange;
    }

    public String getTrustChangeTrust() {
        return trustChangeTrust;
    }

    public void setTrustChangeTrust(String trustChangeName) {
        this.trustChangeTrust = trustChangeName;
    }

    public Integer getWasRefered() {
        return wasRefered;
    }

    public void setWasRefered(Integer wasRefered) {
        this.wasRefered = wasRefered;
    }

    public Integer getWasReferedBy() {
        return wasReferedBy;
    }

    public void setWasReferedBy(Integer wasReferedBy) {
        this.wasReferedBy = wasReferedBy;
    }

    public String getWasReferedByFullName() {
        return wasReferedByFullName;
    }

    public void setWasReferedByFullName(String wasReferedByFullName) {
        this.wasReferedByFullName = wasReferedByFullName;
    }

    public TrustTrustTypeEntity getTrustTypeEntity() {
        return trustTypeEntity;
    }

    public void setTrustTypeEntity(TrustTrustTypeEntity trustTypeEntity) {
        this.trustTypeEntity = trustTypeEntity;
    }

    public CatalogAddressEntity getAddressEntity() {
        return addressEntity;
    }

    public void setAddressEntity(CatalogAddressEntity addressEntity) {
        this.addressEntity = addressEntity;
    }

    public CatalogPersonEntity getPersonEntity() {
        return personEntity;
    }

    public void setPersonEntity(CatalogPersonEntity personEntity) {
        this.personEntity = personEntity;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public SystemUserEntity getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(SystemUserEntity registeredBy) {
        this.registeredBy = registeredBy;
    }

    @Override
    public String getStatusAsString() throws Exception {
        if (this.status < 0 || (this.status > CommonEntity.statuses.length)) {
            throw new Exception("RequestRequestEntity::getStatusAsString::Valor de estatus fuera del rango");
        }

        return CommonEntity.statuses[this.status];
    }

    public String getTrustChangeAsString() throws Exception {
        if (this.trustChange < 0 || (this.trustChange > CommonEntity.simpleOptions.length)) {
            throw new Exception("RequestRequestEntity::getTrustChangeAsString::Valor de trustChange fuera del rango");
        }

        return CommonEntity.simpleOptions[this.trustChange];
    }
    
    public String getWasReferedAsString() throws Exception {
        if (this.wasRefered < 0 || (this.wasRefered > CommonEntity.simpleOptions.length)) {
            throw new Exception("RequestRequestEntity::getWasReferedAsString::Valor de wasRefered fuera del rango");
        }

        return CommonEntity.simpleOptions[this.wasRefered];
    }
    
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("requestId", this.requestId);
        map.put("number", this.number);
        map.put("trustChange", this.trustChange);
        map.put("trustChangeName", this.trustChangeTrust);
        map.put("wasRefered", this.wasRefered);
        map.put("wasReferedBy", this.wasReferedBy);
        map.put("wasReferedByFullName", this.wasReferedByFullName);
        map.put("trustType", this.trustTypeEntity.toMap());
        map.put("address", this.addressEntity.toMap());
        map.put("person", this.personEntity.toMap());
        map.put("state", this.state);
        map.put("status", this.status);
        map.put("created", this.created);

        return map;
    }
    
    
}
