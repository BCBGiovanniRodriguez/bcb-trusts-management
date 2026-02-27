package com.bcb.trust.front.modules.request.model.entity.catalog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.bcb.trust.front.modules.common.model.CommonEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "request_catalog_business_members")
public class BusinessMemberEntity extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long businessMemberId;

    private String code;

    private String name;

    private String description;

    @Column(columnDefinition = "TINYINT")
    private Integer status;

    private LocalDateTime createdAt;

    public Long getBusinessMemberId() {
        return businessMemberId;
    }

    public void setBusinessMemberId(Long businessMemberId) {
        this.businessMemberId = businessMemberId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String getStatusAsString() throws Exception {
        if (this.status < 0 || (this.status > CommonEntity.statuses.length)) {
            throw new Exception("BusinessMemberEntity::getStatusAsString::Valor de estatus fuera del rango");
        }

        return CommonEntity.statuses[this.status];
    }

    public Map<String, Object> toMap() {
        DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Map<String, Object> map = new HashMap<>();
        map.put("businessMemberId", this.businessMemberId);
        map.put("code", this.code);
        map.put("name", this.name);
        map.put("description", this.description);
        map.put("status", this.status);
        map.put("createdAt", this.createdAt != null ? this.createdAt.format(isoFormatter) : null);

        return map;
    }

}
