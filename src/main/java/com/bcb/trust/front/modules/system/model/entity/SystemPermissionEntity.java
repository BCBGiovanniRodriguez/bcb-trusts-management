package com.bcb.trust.front.modules.system.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_permissions")
public class SystemPermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    private String name;

    private String code;

    private Integer module;

    private String route;

    private static String[] moduleNames = {"No definido", "Configuración", "Catálogo", "Solicitud", "Fideicomiso", "Reporte", "Terceros", "Sistema"};

    @Column(name = "path_params", nullable = true)
    private String pathParams;

    @Column(name = "query_params", nullable = true)
    private String queryParams;

    private LocalDateTime created;

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

    @Override
    public String toString() {
        return "SystemPermissionEntity [permissionId=" + permissionId + ", name=" + name + ", code=" + code
                + ", module=" + module + ", route=" + route + ", pathParams=" + pathParams + ", queryParams="
                + queryParams + ", created=" + created + "]";
    }

    public String getModuleAsString() throws Exception {
        if (this.module < 0 || (this.module > moduleNames.length)) {
            throw new Exception("SystemPermissionEntity::getModuleAsString::Valor de modulo fuera del rango");
        }

        return SystemPermissionEntity.moduleNames[this.module];
    }
}
