package com.bcb.trust.front.modules.configuration.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "configuration_endpoints")
public class ConfigurationEndpointEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long endpointId;

    private String url;

    private String port;

    private Integer status;

    private LocalDateTime created;

    public ConfigurationEndpointEntity() {
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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "ConfigurationEndpointEntity [endpointId=" + endpointId + ", url=" + url + ", port=" + port + ", status="
                + status + ", created=" + created + "]";
    }

}
