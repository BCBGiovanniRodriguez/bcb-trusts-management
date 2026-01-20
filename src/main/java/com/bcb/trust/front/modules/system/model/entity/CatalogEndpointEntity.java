package com.bcb.trust.front.modules.system.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_catalog_endpoints")
public class CatalogEndpointEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long endpointId;

    private String url;

    private String port;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer status;

    private LocalDateTime createdAt;

    public CatalogEndpointEntity() {
    }

    public Long getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(Long endpointId) {
        this.endpointId = endpointId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
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

    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
    }

    @Override
    public String toString() {
        return "ConfigurationEndpointEntity [endpointId=" + endpointId + ", url=" + url + ", port=" + port + ", status="
                + status + ", created=" + createdAt + "]";
    }

}
