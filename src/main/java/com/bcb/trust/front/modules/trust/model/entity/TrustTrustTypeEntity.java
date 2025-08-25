package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bcb.trust.front.modules.common.model.CommonEntity;
import com.bcb.trust.front.modules.request.model.entity.RequestRequestEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_trust_types")
public class TrustTrustTypeEntity extends CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trustTypeId;

    private String name;

    private String description;

    private Integer status;

    private LocalDateTime created;

    @OneToMany(mappedBy = "trustTypeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RequestRequestEntity> requestSet;

    public TrustTrustTypeEntity() {
        requestSet = new HashSet<>();
    }

    public Long getTrustTypeId() {
        return trustTypeId;
    }

    public void setTrustTypeId(Long trustTypeId) {
        this.trustTypeId = trustTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "TrustTrustTypeEntity [trustTypeId=" + trustTypeId + ", name=" + name + ", description=" + description
                + ", status=" + status + ", created=" + created + "]";
    }

    public Map<String, Object> toMap() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("profileId", this.trustTypeId);
        map.put("name", this.name);
        map.put("description", this.description);
        map.put("status", this.status);
        map.put("statusAsString", this.getStatusAsString());
        map.put("created", this.created);

        return map;
    }

    @Override
    public String getStatusAsString() throws Exception {
        if (this.status < 0 || (this.status > CommonEntity.statuses.length)) {
            throw new Exception("TrustTrusTypeEntity::getStatusAsString::Valor de estatus fuera del rango");
        }

        return CommonEntity.statuses[this.status];
    }

}
