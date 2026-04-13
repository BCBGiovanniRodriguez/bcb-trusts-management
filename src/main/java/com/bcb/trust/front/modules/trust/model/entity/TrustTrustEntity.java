package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.bcb.trust.front.modules.common.model.CommonEntity;
import com.bcb.trust.front.modules.request.model.entity.RequestRequestEntity;
import com.bcb.trust.front.modules.request.model.entity.catalog.BusinessTypeEntity;
import com.bcb.trust.front.modules.system.model.entity.CatalogUserEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_catalog_trusts")
public class TrustTrustEntity extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trustId;

    private Integer number;

    private String name;

    @OneToOne
    @JoinColumn(name = "requestId", referencedColumnName = "requestId")
    private RequestRequestEntity requestEntity;

    @ManyToOne
    @JoinColumn(name = "business_type_id", nullable = false)
    private BusinessTypeEntity businessTypeEntity;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer state;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer status;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "registered_by", nullable = false)
    private CatalogUserEntity registeredBy;

    @OneToMany(mappedBy = "trust", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrustTrustorEntity> trustorList = new ArrayList<>();

    @OneToMany(mappedBy = "trustEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrustTrusteeEntity> trusteeList = new ArrayList<>();

    @OneToMany(mappedBy = "trustEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrustWorkerDepartmentEntity> workerDepartmentList = new ArrayList<>();

    @OneToMany(mappedBy = "trustEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrustWorkerMovementType> workerMovementTypeList = new ArrayList<>();

    @OneToMany(mappedBy = "trustEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrustSpecialWorkerEntity> specialWorkerList = new ArrayList<>();
    
    @OneToMany(mappedBy = "trustEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrustSpecialYearEntity> specialYearList = new ArrayList<>();
    
    @OneToMany(mappedBy = "trustEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrustSpecialPeriodEntity> specialPeriodList = new ArrayList<>();

    public static final Integer STATE_STARTED = 1;

    public static final Integer STATE_PAUSED = 2;

    public static final Integer STATE_LOCKED = 3;

    public static final Integer STATE_ACTIVE = 4;

    public static final Integer STATE_FINISHED = 5;

    public static String[] states = { "Seleccione Opción", "Iniciado", "Pausado", "Bloqueado", "Constituido",
            "Finalizado" };

    public TrustTrustEntity() {
    }

    public Long getTrustId() {
        return trustId;
    }

    public void setTrustId(Long trustId) {
        this.trustId = trustId;
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

    @Override
    public String toString() {
        return "TrustTrustEntity [trustId=" + trustId + ", number=" + number + ", name=" + name + ", state=" + state + ", status=" + status + ", created=" + createdAt + "]";
    }

    @Override
    public String getStatusAsString() throws Exception {
        if (this.status < 0 || (this.status > CommonEntity.statuses.length)) {
            throw new Exception("SystemProfileEntity::getStatusAsString::Valor de estatus fuera del rango");
        }

        return CommonEntity.statuses[this.status];
    }

    public String getStateAsString() {
        return TrustTrustEntity.states[this.state];
    }

    public RequestRequestEntity getRequestEntity() {
        return requestEntity;
    }

    public void setRequestEntity(RequestRequestEntity requestEntity) {
        this.requestEntity = requestEntity;
    }

    public List<TrustTrustorEntity> getTrustorList() {
        return trustorList;
    }

    public void setTrustorList(List<TrustTrustorEntity> trustorList) {
        this.trustorList = trustorList;
    }

    public List<TrustTrusteeEntity> getTrusteeList() {
        return trusteeList;
    }

    public void setTrusteeList(List<TrustTrusteeEntity> trusteeList) {
        this.trusteeList = trusteeList;
    }

    public CatalogUserEntity getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(CatalogUserEntity registeredBy) {
        this.registeredBy = registeredBy;
    }

    public BusinessTypeEntity getBusinessTypeEntity() {
        return businessTypeEntity;
    }

    public void setBusinessTypeEntity(BusinessTypeEntity businessTypeEntity) {
        this.businessTypeEntity = businessTypeEntity;
    }
}
