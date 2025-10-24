package com.bcb.trust.front.modules.catalog.model.entity;

import java.time.LocalDateTime;

import com.bcb.trust.front.modules.common.model.CommonEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "catalog_file_subtypes")
public class CatalogFileSubtypeEntity extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileSubtypeId;

    private String name;

    private Integer mimeType;

    private Integer status;

    private LocalDateTime created;

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
            throw new Exception("CatalogFileSubtypeEntity::getStatusAsString::Valor de estatus fuera del rango");
        }

        return CommonEntity.statuses[this.status];
    }

    

}
