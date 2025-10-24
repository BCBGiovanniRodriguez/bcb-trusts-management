package com.bcb.trust.front.modules.catalog.model.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.bcb.trust.front.modules.common.model.CommonEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "catalog_document_types")
public class CatalogDocumentTypeEntity extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentTypeId;

    private String name;

    private String description;

    private Integer status;

    private Integer ownerType;

    private Integer personType;

    private Integer foreignStatus;

    private LocalDateTime created;

    public static final Integer OWNER_TYPE_APPLICANT = 1;

    public static final Integer OWNER_TYPE_THIRD = 2;

    public static final String[] ownerTypes = {"", "Solicitante", "Apoderado Legal"};

    public static final Integer TYPE_PERSON = 1;

    public static final Integer TYPE_ENTERPRISE = 2;

    public static final String[] personTypes = {"Seleccione Opción", "Persona Física", "Persona Moral"};

    public static final Integer FOREIGN_STATUS_CITIZEN = 1;

    public static final Integer FOREIGN_STATUS_FOREIGNER = 2;

    public static final String[] foreignStatuses = {"Seleccione Opción", "Nacional", "Extranjero"};

    public CatalogDocumentTypeEntity() {
    }

    public Long getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Long documentTypeId) {
        this.documentTypeId = documentTypeId;
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
    public String getStatusAsString() throws Exception {
        if (this.status < 0 || (this.status > CommonEntity.statuses.length)) {
            throw new Exception("CatalogDocumentTypeEntity::getStatusAsString::Valor de estatus fuera del rango");
        }

        return CommonEntity.statuses[this.status];
    }

    @Override
    public String toString() {
        return "CatalogDocumentType [documentTypeId=" + documentTypeId + ", name=" + name + ", description="
                + description + ", status=" + status + ", created=" + created + "]";
    }

    public Map<String, Object> toMap() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("documentTypeId", this.documentTypeId);
        map.put("name", this.name);
        map.put("name", this.description);
        map.put("status", this.status);
        map.put("statusAsString", this.getStatusAsString());
        map.put("created", this.created);

        return map;
    }

    public Integer getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Integer ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerTypeAsString() throws Exception {
        if (this.ownerType < 0 || (this.ownerType > CatalogDocumentTypeEntity.ownerTypes.length)) {
            throw new Exception("CatalogDocumentTypeEntity::getOwnerTypeAsString::Valor de pertenencia fuera del rango");
        }

        return CatalogDocumentTypeEntity.ownerTypes[this.ownerType];
    }

    public Integer getPersonType() {
        return personType;
    }

    public void setPersonType(Integer personType) {
        this.personType = personType;
    }

    public Integer getForeignStatus() {
        return foreignStatus;
    }

    public void setForeignStatus(Integer foreignStatus) {
        this.foreignStatus = foreignStatus;
    }
    
    public String getPersonTypeAsString() throws Exception {
        if (this.personType < 0 || (this.personType > CatalogDocumentTypeEntity.personTypes.length)) {
            throw new Exception("CatalogDocumentTypeEntity::getPersonTypeAsString::Valor de tipo de persona fuera del rango");
        }

        return CatalogDocumentTypeEntity.personTypes[this.personType];
    }

    public String getForeignStatusAsString() throws Exception {
        if (this.foreignStatus < 0 || (this.foreignStatus > CatalogDocumentTypeEntity.foreignStatuses.length)) {
            throw new Exception("CatalogDocumentTypeEntity::getForeignStatusAsString::Valor de estatus migratorio fuera del rango");
        }

        return CatalogDocumentTypeEntity.foreignStatuses[this.foreignStatus];
    }
}
