package com.bcb.trust.front.modules.catalog.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.bcb.trust.front.modules.common.model.CommonEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "catalog_file_types")
public class CatalogFileTypeEntity extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileTypeId;

    private String name;

    private Integer status;

    private LocalDateTime created;

    @OneToMany(mappedBy = "fileType", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<CatalogFileSubtypeEntity> fileSubtypes;

    public CatalogFileTypeEntity() {
    }

    public Long getFileTypeId() {
        return fileTypeId;
    }

    public void setFileTypeId(Long fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public String getStatusAsString() throws Exception {
        if (this.status < 0 || (this.status > CommonEntity.statuses.length)) {
            throw new Exception("CatalogFileTypeEntity::getStatusAsString::Valor de estatus fuera del rango");
        }

        return CommonEntity.statuses[this.status];
    }

    
}
