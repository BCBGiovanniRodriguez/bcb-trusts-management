package com.bcb.trust.front.modules.system.model.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.bcb.trust.front.modules.common.model.CommonEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_permissions")
public class SystemPermissionEntity extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    private String name;

    private String code;

    private Integer module;

    private String route;

    @Column(name = "path_params", nullable = true)
    private String pathParams;
    
    @Column(name = "query_params", nullable = true)
    private String queryParams;

    private Integer status;
    
    private LocalDateTime created;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.EAGER)
    private Set<SystemProfileEntity> profiles = new HashSet<>();

    public static String[] moduleNames = {"Seleccione Opción", "No definido", "Configuración", "Catálogos", "Administración", "Reportes", "Sistema"};

    public SystemPermissionEntity() {
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getPathParams() {
        return pathParams;
    }

    public void setPathParams(String pathParams) {
        this.pathParams = pathParams;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Integer getModule() {
        return module;
    }

    public void setModule(Integer module) {
        this.module = module;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getModuleAsString() throws Exception {
        if (this.module < 0 || (this.module > moduleNames.length)) {
            throw new Exception("SystemPermissionEntity::getModuleAsString::Valor de modulo fuera del rango");
        }

        return SystemPermissionEntity.moduleNames[this.module];
    }

    @Override
    public String toString() {
        return "SystemPermissionEntity [permissionId=" + permissionId + ", name=" + name + ", code=" + code
                + ", module=" + module + ", route=" + route + ", pathParams=" + pathParams + ", queryParams="
                + queryParams + ", status=" + status + ", created=" + created + "]";
    }

    @Override
    public String getStatusAsString() throws Exception {
        if (this.status < 0 || (this.status > CommonEntity.statuses.length)) {
            throw new Exception("SystemPermissionEntity::getStatusAsString::Valor de estatus fuera del rango");
        }

        return CommonEntity.statuses[this.status];
    }

    public Set<SystemProfileEntity> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<SystemProfileEntity> profiles) {
        this.profiles = profiles;
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
        SystemPermissionEntity other = (SystemPermissionEntity) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }

    

    
}
