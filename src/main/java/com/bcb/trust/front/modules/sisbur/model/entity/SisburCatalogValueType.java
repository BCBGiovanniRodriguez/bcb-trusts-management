package com.bcb.trust.front.modules.sisbur.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "sisbur_catalog_value_types")
public class SisburCatalogValueType {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long valueTypeId;

    private String name;

    private String description;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer status;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "valueType")
    private List<SisburCatalogInstrument> instruments;

    public SisburCatalogValueType() {
    }

    public Long getValueTypeId() {
        return valueTypeId;
    }

    public void setValueTypeId(Long valueTypeId) {
        this.valueTypeId = valueTypeId;
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
    public String toString() {
        return "SisburCatalogValueType [valueTypeId=" + valueTypeId + ", name=" + name + ", description=" + description
                + ", status=" + status + ", createdAt=" + createdAt + "]";
    }

}
