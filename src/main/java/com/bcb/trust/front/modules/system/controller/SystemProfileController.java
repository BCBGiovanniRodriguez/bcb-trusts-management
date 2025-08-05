package com.bcb.trust.front.modules.system.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcb.trust.front.modules.system.model.entity.SystemPermissionEntity;
import com.bcb.trust.front.modules.system.model.entity.SystemProfileEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemPermissionRepository;
import com.bcb.trust.front.modules.system.model.repository.SystemProfileRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/system")
public class SystemProfileController {

    @Autowired
    private SystemPermissionRepository systemPermissionRepository;

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
    public String saveProfile(@RequestBody Map<String, Object> data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();
        
        try {
            if (data.size() < 0 ) {
                throw new Exception("No se envíaron datos");
            }

            SystemProfileEntity profileEntity = new SystemProfileEntity();
            
            if (!data.containsKey("name")) {
                throw new Exception("Nombre es requerido");
            }

            String members = (String) data.get("members");
            System.out.println(members);
            if (members != null || members.length() == 0) {
                profileEntity.setMembers(Integer.parseInt(members));
            }

            profileEntity.setName(data.get("name").toString());
            profileEntity.setStatus(1);
            profileEntity.setCreated(LocalDateTime.now());

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
    
    
    @PutMapping("/profile/{id}")
    public String putProfile(@PathVariable Long id, @RequestBody Map<String, Object> data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();
        SystemProfileEntity profileEntity = null;

        try {
            Optional<SystemProfileEntity> result = systemProfileRepository.findById(id);

            if (!result.isPresent()) {
                throw new Exception("Registro con identificador: " + id + " no encontrado");
            } else {
                // Get data from request body
                String name = data.get("name").toString();
                Integer members = null;
                Object membersObj = data.get("members");
                List<Long> permissionList = (ArrayList<Long>) data.get("permissionIds");

                if (membersObj != null) {
                    members = Integer.parseInt(membersObj.toString());
                }
                
                profileEntity = result.get();
                profileEntity.setName(name);
                profileEntity.setMembers(members);
                
                if (permissionList.size() > 0) {
                    List<SystemPermissionEntity> permissionEntityList = systemPermissionRepository.findAllById(permissionList);
                    
                    profileEntity.setPermissions(new HashSet<>());
                    
                    for (SystemPermissionEntity permissionEntity : permissionEntityList) {
                        profileEntity.getPermissions().add(permissionEntity);
                    }
                }

                systemProfileRepository.saveAndFlush(profileEntity);
            }

            resultMap.put("status", 1);
            resultMap.put("message", "Petición Correcta");
            resultMap.put("data", profileEntity.toMap());
            
            jsonResponse = mapper.writeValueAsString(resultMap);

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            resultMap.put("status", 0);
            resultMap.put("message", "Error en SystemProfileController::put[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }
        
        return jsonResponse;
    }
}
