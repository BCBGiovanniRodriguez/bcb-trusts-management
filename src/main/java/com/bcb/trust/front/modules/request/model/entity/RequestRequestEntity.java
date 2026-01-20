package com.bcb.trust.front.modules.request.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bcb.trust.front.modules.common.model.CommonEntity;
import com.bcb.trust.front.modules.system.model.entity.CatalogAddressEntity;
import com.bcb.trust.front.modules.system.model.entity.CatalogPersonEntity;
import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustCatalogTrustTypeEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrusteeEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustorEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "request_catalog_requests")
public class RequestRequestEntity extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "trust_type_id", nullable = false)
    private TrustCatalogTrustTypeEntity trustTypeEntity;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private CatalogAddressEntity addressEntity;

    @Column(nullable = false)
    private Integer number;

    private String name;

    @ManyToOne
    @JoinColumn(name = "personId", nullable = false)
    private CatalogPersonEntity personEntity;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer isTrustChange;

    private String trustChangeName;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer state;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer status;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "registered_by", nullable = false)
    private SystemUserEntity registeredBy;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrustTrustorEntity> trustorList = new ArrayList<>();

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrustTrusteeEntity> trusteeList = new ArrayList<>();

    public static final Integer WAS_REFERED_BY_UNKOWN = 0;

    public static final Integer WAS_REFERED_BY_TRUST = 1;

    public static final Integer WAS_REFERED_BY_LAW = 2;

    public static final Integer WAS_REFERED_BY_CONSULTANT = 3;

    public static final Integer WAS_REFERED_BY_SPONSOR = 4;

    public static final Integer WAS_REFERED_BY_OTHER = 5;

    public static String[] wasRefereredBy = { "Seleccione Opción", "Fiduciario", "Legal", "Asesor", "Promotor",
            "Otro" };

    public static final Integer STATE_REGISTERED = 1;

    public static final Integer STATE_PLD_VALIDATION = 2;

    public static final Integer STATE_PLD_VALIDATED = 3;

    public static final Integer STATE_NEXT_A = 4;

    public static final Integer STATE_NEXT_B = 4;

    public static final Integer STATE_NEXT_C = 4;

    public static final Integer STATE_NEXT_D = 4;

    public RequestRequestEntity() {
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsTrustChange() {
        return isTrustChange;
    }

    public void setIsTrustChange(Integer trustChange) {
        this.isTrustChange = trustChange;
    }

    public String getTrustChangeName() {
        return trustChangeName;
    }

    public void setTrustChangeName(String trustChangeName) {
        this.trustChangeName = trustChangeName;
    }

    public TrustCatalogTrustTypeEntity getTrustTypeEntity() {
        return trustTypeEntity;
    }

    public void setTrustTypeEntity(TrustCatalogTrustTypeEntity trustTypeEntity) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
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
        if (this.isTrustChange < 0 || (this.isTrustChange > CommonEntity.simpleOptions.length)) {
            throw new Exception("RequestRequestEntity::getTrustChangeAsString::Valor de trustChange fuera del rango");
        }

        return CommonEntity.simpleOptions[this.isTrustChange];
    }

    public Map<String, Object> toMap() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("requestId", this.requestId);
        map.put("number", this.number);
        map.put("name", this.name);
        map.put("trustChange", this.isTrustChange);
        map.put("trustChangeName", this.trustChangeName);
        map.put("trustType", this.trustTypeEntity.toMap());
        map.put("address", this.addressEntity.toMap());
        map.put("person", this.personEntity.toMap());
        map.put("state", this.state);
        map.put("status", this.status);
        map.put("created", this.createdAt);

        return map;
    }

}
