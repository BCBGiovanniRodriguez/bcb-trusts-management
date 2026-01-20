package com.bcb.trust.front.modules.system.model.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_catalog_resources")
public class SystemResourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resourceId;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer module;

    private String code;

    private String element;

    private String action;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "resourceEntity")
    private List<SystemUserActivityEntity> userActivityList;

    public static final Integer MODULE_UNDEFINED = 1;

    public static final Integer MODULE_SYSTEM = 2;

    public static final Integer MODULE_ADMINISTRATION = 3;

    public static final Integer MODULE_OPERATION = 4;

    public static final Integer MODULE_ACCOUNTING = 5;

    public static final Integer MODULE_REPORTS = 6;

    public static final Integer MODULE_PLD = 7;

    public static String[] moduleNames = { "Seleccione Opción", "No definido", "Sistema", "Administración",
            "Operaciones", "Contabilidad", "Reportes", "PLD" };

    public SystemResourceEntity() {
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getModule() {
        return module;
    }

    public void setModule(Integer module) {
        this.module = module;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAction() {
        return action;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public void setAction(String name) {
        this.action = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
    }

    @Override
    public String toString() {
        return "SystemResourceEntity [resourceId=" + resourceId + ", code=" + code + ", name=" + action
                + ", createdAt=" + createdAt + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SystemResourceEntity other = (SystemResourceEntity) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }

    public Map<String, Object> toMap() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("resourceId", this.resourceId);
        map.put("name", this.action);
        map.put("code", this.code);
        map.put("created", this.createdAt);

        return map;
    }

    public String getModuleAsString() throws Exception {
        if (this.module < 0 || (this.module > SystemResourceEntity.moduleNames.length)) {
            throw new Exception("CatalogPersonEntity::getStatusAsString::Valor de tipo fuera del rango");
        }

        return SystemResourceEntity.moduleNames[this.module];
    }

}
