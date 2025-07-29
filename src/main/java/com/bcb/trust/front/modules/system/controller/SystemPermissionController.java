package com.bcb.trust.front.modules.system.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcb.trust.front.modules.system.model.entity.SystemPermissionEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemPermissionRepository;
import com.bcb.trust.front.modules.system.model.validator.SystemPermissionValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@RequestMapping("/api/system")
public class SystemPermissionController {

    @Autowired
    private SystemPermissionRepository systemPermissionRepository;

    @GetMapping("/permission")
    public String get() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();
        List<SystemPermissionEntity> permissionList;

        try {
            permissionList = systemPermissionRepository.findAll();

            resultMap.put("status", 1);
            resultMap.put("message", "Petición Correcta");
            resultMap.put("data", permissionList);
            
            jsonResponse = mapper.writeValueAsString(resultMap);
        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error en SystemPermissionController::getAll[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }

        return jsonResponse;
    }

    @PostMapping("/permission")
    public String post(@RequestBody SystemPermissionEntity systemPermissionEntity) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();
        List<String> errorList = new ArrayList<>();
        
        try {
            SystemPermissionValidator systemPermissionValidator = new SystemPermissionValidator();
            systemPermissionValidator.setSystemPermissionEntity(systemPermissionEntity);
            systemPermissionValidator.setSystemPermissionRepository(systemPermissionRepository);
            systemPermissionValidator.validate(true);

            if (systemPermissionValidator.hasErrors()) {
                errorList = systemPermissionValidator.getErrors();
                throw new Exception(errorList.toString());
            } else {
                systemPermissionEntity.setQueryParams("");
                systemPermissionEntity.setPathParams("");
                systemPermissionEntity.setRoute("");
                systemPermissionEntity.setCreated(LocalDateTime.now());

                systemPermissionRepository.saveAndFlush(systemPermissionEntity);
            }

            resultMap.put("status", 1);
            resultMap.put("message", "Petición Correcta");
            resultMap.put("data", systemPermissionEntity);

            jsonResponse = mapper.writeValueAsString(resultMap);
        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error en: SystemPermissionController::post[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }

        return jsonResponse;
    }
    
    
}
