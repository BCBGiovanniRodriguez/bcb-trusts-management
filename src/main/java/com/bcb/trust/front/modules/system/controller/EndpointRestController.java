package com.bcb.trust.front.modules.system.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcb.trust.front.modules.system.model.entity.CatalogEndpointEntity;
import com.bcb.trust.front.modules.system.model.repository.CatalogEndpointRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/system/configuration")
public class EndpointRestController {

    @Autowired
    private CatalogEndpointRepository endpointRepository;

    @PostMapping("/endpoint")
    public String post( @RequestBody Map<String, Object> data, Authentication authentication) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();
        
        try {
            if (data != null) {
                /*
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    System.out.println(entry.getKey() + " = " + entry.getValue());
                }*/

                CatalogEndpointEntity endpointEntity = new CatalogEndpointEntity();
                endpointEntity.setType(Integer.parseInt(data.get("type").toString()));
                endpointEntity.setCode(Integer.parseInt(data.get("code").toString()));
                endpointEntity.setEnv(Integer.parseInt(data.get("env").toString()));
                endpointEntity.setUsername(data.get("username") != null ? (String) data.get("username").toString() : null);
                endpointEntity.setPassword(data.get("password") != null ? (String) data.get("password").toString() : null);
                endpointEntity.setToken(data.get("token") != null ? (String) data.get("token").toString() : null);
                endpointEntity.setUrl(data.get("url") != null ? (String) data.get("url").toString() : null);
                endpointEntity.setPort(data.get("port") != null ? (String) data.get("port").toString() : null);
                endpointEntity.setResource(data.get("resource") != null ? (String) data.get("resource").toString() : null);
                endpointEntity.setParameters(data.get("parameters") != null ? (String) data.get("parameters").toString() : "");
                endpointEntity.setStatus(1);
                endpointEntity.setCreatedAt(LocalDateTime.now());
                
                endpointRepository.save(endpointEntity);

                resultMap.put("status", 1);
                resultMap.put("message", "Recursos agregados correctamente");
                resultMap.put("data", endpointEntity.toMap());
            }
            
        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error en EndpointRestController::post[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);
        }
        
        jsonResponse = mapper.writeValueAsString(resultMap);
        return jsonResponse;
    }

    @PutMapping("/endpoint/{id}")
    public String put(@PathVariable Long id, @RequestBody Map<String, Object> data, Authentication authentication) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();
        
        try {
            if (data != null) {
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    System.out.println(entry.getKey() + " = " + entry.getValue());
                }

                CatalogEndpointEntity endpointEntity = endpointRepository
                    .findById(id)
                    .orElse(null);

                if (endpointEntity != null) {
                    Integer typeTmp = Integer.parseInt(data.get("type").toString());
                    Integer codeTmp = Integer.parseInt(data.get("code").toString());
                    Integer envTmp = Integer.parseInt(data.get("env").toString());
                    String usernameTmp = data.get("username") != null ? (String) data.get("username").toString() : null;
                    String passwordTmp = data.get("password") != null ? (String) data.get("password").toString() : null;
                    String tokenTmp = data.get("token") != null ? (String) data.get("token").toString() : null;
                    String urlTmp = data.get("url") != null ? (String) data.get("url").toString() : null;
                    String portTmp = data.get("port") != null ? (String) data.get("port").toString() : null;
                    String resourceTmp = data.get("resource") != null ? (String) data.get("resource").toString() : null;

                    if (endpointEntity.getType() != typeTmp) endpointEntity.setType(typeTmp);
                    if (endpointEntity.getCode() != codeTmp) endpointEntity.setCode(codeTmp);
                    if (endpointEntity.getEnv() != envTmp) endpointEntity.setEnv(envTmp);
                    if ((endpointEntity.getUsername() == null && usernameTmp != null) || (endpointEntity.getUsername() != null && !endpointEntity.getUsername().equals(usernameTmp))) endpointEntity.setUsername(usernameTmp);
                    if ((endpointEntity.getPassword() == null && passwordTmp != null) || (endpointEntity.getPassword() != null && !endpointEntity.getPassword().equals(passwordTmp))) endpointEntity.setPassword(passwordTmp);
                    if ((endpointEntity.getToken() == null && tokenTmp != null) || (endpointEntity.getToken() != null && !endpointEntity.getToken().equals(tokenTmp))) endpointEntity.setToken(tokenTmp);
                    if ((endpointEntity.getUrl() == null && urlTmp != null) || (endpointEntity.getUrl() != null && !endpointEntity.getUrl().equals(urlTmp))) endpointEntity.setUrl(urlTmp);
                    if ((endpointEntity.getPort() == null && portTmp != null) || (endpointEntity.getPort() != null && !endpointEntity.getPort().equals(portTmp))) endpointEntity.setPort(portTmp);
                    if ((endpointEntity.getResource() == null && resourceTmp != null) || (endpointEntity.getResource() != null && !endpointEntity.getResource().equals(resourceTmp))) endpointEntity.setResource(resourceTmp);
                    
                    endpointRepository.save(endpointEntity);

                    resultMap.put("status", 1);
                    resultMap.put("message", "Recursos actualizados correctamente");
                    resultMap.put("data", endpointEntity.toMap());
                } else {
                    resultMap.put("status", 0);
                    resultMap.put("message", "Endpoint no encontrado");
                }
            }
            
        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error en EndpointRestController::put[" + e.getMessage() + "]");
            resultMap.put("data", null);
        }
        
        jsonResponse = mapper.writeValueAsString(resultMap);
        return jsonResponse;
    }
    
}
