package com.bcb.trust.front.modules.system.model.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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

    @Column(columnDefinition = "TINYINT(1)")
    private Integer type;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer code;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer env;

    private String username;

    private String password;

    private String token;

    private String url;

    private String port;

    private String resource;

    private String parameters;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer status;

    private LocalDateTime createdAt;

    public static final Integer TYPE_REST = 1;

    public static final Integer TYPE_SOAP = 2;

    public static String[] typeNames = {"Desconocido", "REST", "SOAP" };
    
    public static final Integer CODE_PLD = 1;
    
    public static final Integer CODE_LOCATION = 2;
    
    public static final Integer CODE_SISBUR = 3;
    
    public static String[] codeNames = {"Desconocido", "MINDS_PLD", "BCB_WS_LOCATION", "BCB_WS_SISBUR" };

    public static final Integer ENV_PRD = 1;
    
    public static final Integer ENV_QA = 2;
    
    public static final Integer ENV_DEV = 3;
    
    public static String[] envNames = {"Desconocido", "Producción", "QA", "Desarrollo" };

    public CatalogEndpointEntity() {
    }

    public Long getEndpointId() {
        return endpointId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getEnv() {
        return env;
    }

    public void setEnv(Integer env) {
        this.env = env;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEndpointId(Long endpointId) {
        this.endpointId = endpointId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
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

    public String getTypeAsString() throws Exception {
        if (this.type < 0 || (this.type > CatalogEndpointEntity.typeNames.length)) {
            throw new Exception("CatalogEndpointEntity::getTypeAsString::Valor de tipo fuera del rango");
        }

        return CatalogEndpointEntity.typeNames[this.type];
    }
    
    public String getEnvAsString() throws Exception {
        if (this.env < 0 || (this.env > CatalogEndpointEntity.envNames.length)) {
            throw new Exception("CatalogEndpointEntity::getEnvAsString::Valor de env fuera del rango");
        }

        return CatalogEndpointEntity.envNames[this.env];
    }
    
    public String getCodeAsString() throws Exception {
        if (this.code < 0 || (this.code > CatalogEndpointEntity.codeNames.length)) {
            throw new Exception("CatalogEndpointEntity::getCodeAsString::Valor de código fuera del rango");
        }

        return CatalogEndpointEntity.codeNames[this.code];
    }

    public Map<String, Object> toMap() {
        DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        Map<String, Object> map = new HashMap<>();
        map.put("endpoint_id", this.endpointId);
        map.put("type", this.type);
        map.put("code", this.code);
        map.put("env", this.env);
        map.put("username", this.username);
        map.put("password", this.password);
        map.put("token", this.token);
        map.put("url", this.url);
        map.put("port", this.port);
        map.put("resource", this.resource);
        map.put("parameters", this.parameters);
        map.put("status", this.status);
        map.put("created_at", this.createdAt != null ? this.createdAt.format(isoFormatter) : null);

        return map;
    }
}
