package com.bcb.trust.front.model.trusts.entity.system;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SystemPermission")
public class SystemPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PermissionId;

    private String Name;

    private String Code;

    private String Route;

    private String PathParams;

    private String QueryParams;

    private Integer Status;

    private LocalDateTime Created;

    public SystemPermission() {
    }

    public Long getPermissionId() {
        return PermissionId;
    }

    public void setPermissionId(Long permissionId) {
        PermissionId = permissionId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getRoute() {
        return Route;
    }

    public void setRoute(String route) {
        Route = route;
    }

    public String getPathParams() {
        return PathParams;
    }

    public void setPathParams(String pathParams) {
        PathParams = pathParams;
    }

    public String getQueryParams() {
        return QueryParams;
    }

    public void setQueryParams(String queryParams) {
        QueryParams = queryParams;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public LocalDateTime getCreated() {
        return Created;
    }

    public void setCreated(LocalDateTime created) {
        Created = created;
    }

    @Override
    public String toString() {
        return "Permission [PermissionId=" + PermissionId + ", Name=" + Name + ", Code=" + Code + ", Route=" + Route
                + ", PathParams=" + PathParams + ", QueryParams=" + QueryParams + ", Status=" + Status + ", Created="
                + Created + "]";
    }
    
}
