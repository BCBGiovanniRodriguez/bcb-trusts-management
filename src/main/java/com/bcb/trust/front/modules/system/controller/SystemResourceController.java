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

import com.bcb.trust.front.modules.system.model.entity.SystemResourceEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemResourceRepository;
import com.bcb.trust.front.modules.system.model.validator.SystemResourceValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@RequestMapping("/api/system")
public class SystemResourceController {

    @Autowired
    private SystemResourceRepository systemResourceRepository;

    @GetMapping("/resource")
    public String get() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();
        List<SystemResourceEntity> resourceList;

        try {
            resourceList = systemResourceRepository.findAll();

            resultMap.put("status", 1);
            resultMap.put("message", "Petición Correcta");
            resultMap.put("data", resourceList);
            
            jsonResponse = mapper.writeValueAsString(resultMap);
        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error en SystemResourceController::getAll[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }

        return jsonResponse;
    }

    @PostMapping("/resource")
    public String post(@RequestBody SystemResourceEntity systemResourceEntity) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();
        List<String> errorList = new ArrayList<>();
        
        try {
            SystemResourceValidator systemResourceValidator = new SystemResourceValidator();
            systemResourceValidator.setSystemResourceEntity(systemResourceEntity);
            systemResourceValidator.setSystemResourceRepository(systemResourceRepository);
            systemResourceValidator.validate(true);

            if (systemResourceValidator.hasErrors()) {
                errorList = systemResourceValidator.getErrors();
                throw new Exception(errorList.toString());
            } else {
                //systemResourceEntity.setQueryParams("");
                //systemResourceEntity.setPathParams("");
                //systemResourceEntity.setRoute("");
                systemResourceEntity.setCreatedAt(LocalDateTime.now());

                systemResourceRepository.saveAndFlush(systemResourceEntity);
            }

            resultMap.put("status", 1);
            resultMap.put("message", "Petición Correcta");
            resultMap.put("data", systemResourceEntity);

            jsonResponse = mapper.writeValueAsString(resultMap);
        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error en: SystemResourceController::post[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }

        return jsonResponse;
    }
    
    
}
