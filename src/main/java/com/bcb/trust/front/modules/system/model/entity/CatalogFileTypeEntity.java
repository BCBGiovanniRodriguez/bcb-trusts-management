package com.bcb.trust.front.modules.system.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_catalog_file_types")
public class CatalogFileTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileTypeId;

    private String name;

    private LocalDateTime createdAt;

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
    }

}
