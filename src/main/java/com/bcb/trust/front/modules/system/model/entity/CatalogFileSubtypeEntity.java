package com.bcb.trust.front.modules.system.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_catalog_file_subtypes")
public class CatalogFileSubtypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileSubtypeId;

    private String name;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer mimeType;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "file_type_id", nullable = false)
    private CatalogFileTypeEntity fileType;

    public static final Integer MIME_TYPE_UNKNOWN = 0;

    public static final Integer MIME_TYPE_PDF = 1;

    public static final Integer MIME_TYPE_JPEG = 2;
    
    public static final Integer MIME_TYPE_PNG = 3;

    public static final String[] mimeTypes = {"Desconocido", "pdf", "jpeg", "png"};

    public CatalogFileSubtypeEntity() {
    }

    public Long getFileSubtypeId() {
        return fileSubtypeId;
    }

    public void setFileSubtypeId(Long fileSubtypeId) {
        this.fileSubtypeId = fileSubtypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMimeType() {
        return mimeType;
    }

    public void setMimeType(Integer mimeType) {
        this.mimeType = mimeType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
    }

}
