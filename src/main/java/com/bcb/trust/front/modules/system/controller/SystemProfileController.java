package com.bcb.trust.front.modules.system.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcb.trust.front.modules.system.model.entity.SystemProfileEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemProfileRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@RequestMapping("/api")
public class SystemProfileController {

    @Autowired
    private SystemProfileRepository systemProfileRepository;

    @GetMapping("/profile")
    public String getProfiles() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();
        List<SystemProfileEntity> profileEntityList = null;

        try {
            profileEntityList =  systemProfileRepository.findAll();
            
            resultMap.put("status", 1);
            resultMap.put("message", "Petición Correcta");
            resultMap.put("data", profileEntityList);
            
            jsonResponse = mapper.writeValueAsString(resultMap);
        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error en SystemProfileController::getAll[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }
        
        return jsonResponse;
    }

    @PostMapping("/profile")
    public String saveProfile(@RequestBody SystemProfileEntity profileEntity) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();
        
        try {
            if (profileEntity.getName() == null) {
                throw new Exception("Nombre es requerido");
            }

            profileEntity.setStatus(1);
            profileEntity.setCreated(new Date());

            systemProfileRepository.saveAndFlush(profileEntity);

            resultMap.put("status", 1);
            resultMap.put("message", "Petición Correcta");
            resultMap.put("data", profileEntity);

            jsonResponse = mapper.writeValueAsString(resultMap);
        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error en: SystemProfileController::post[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }
        
        return jsonResponse;
    }
    
    
}
