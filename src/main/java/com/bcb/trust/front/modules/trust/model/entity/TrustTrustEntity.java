package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDateTime;

import com.bcb.trust.front.modules.common.model.CommonEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_trusts")
public class TrustTrustEntity extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trustId;

    private Integer number;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trustTypeId", referencedColumnName = "trustTypeId")
    private TrustTrustTypeEntity trustTypeEntity;

    private Integer state;

    private Integer status;

    private LocalDateTime created;

    public static final Integer STATE_STARTED = 1;

    public static final Integer STATE_PAUSED = 2;

    public static final Integer STATE_LOCKED = 3;

    public static final Integer STATE_ACTIVE = 4;

    public static final Integer STATE_FINISHED = 5;

    public static String[] states = {"Seleccione Opción", "Iniciado", "Pausado", "Bloqueado", "Constituido", "Finalizado"};

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

    public TrustTrustTypeEntity getTrustTypeEntity() {
        return trustTypeEntity;
    }

    public void setTrustTypeEntity(TrustTrustTypeEntity trustTypeEntity) {
        this.trustTypeEntity = trustTypeEntity;
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

    @Override
    public String toString() {
        return "TrustTrustEntity [trustId=" + trustId + ", number=" + number + ", name=" + name + ", trustTypeEntity="
                + trustTypeEntity + ", state=" + state + ", status=" + status + ", created=" + created + "]";
    }

    @Override
    public String getStatusAsString() throws Exception {
        if (this.status < 0 || (this.status > CommonEntity.statuses.length)) {
            throw new Exception("SystemProfileEntity::getStatusAsString::Valor de estatus fuera del rango");
        }

        return CommonEntity.statuses[this.status];
    }
}
