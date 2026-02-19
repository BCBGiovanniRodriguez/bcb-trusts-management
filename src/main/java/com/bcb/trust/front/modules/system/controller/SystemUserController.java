package com.bcb.trust.front.modules.system.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcb.trust.front.modules.system.model.entity.CatalogProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.CatalogUserEntity;
import com.bcb.trust.front.modules.system.model.entity.ConfigurationUserProfileEntity;
import com.bcb.trust.front.modules.system.model.repository.ConfigurationUserProfileRepository;
import com.bcb.trust.front.modules.system.model.repository.ProfileRepository;
import com.bcb.trust.front.modules.system.model.repository.UserEntityRepository;
import com.bcb.trust.front.modules.system.service.SystemUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/system")
public class SystemUserController {

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ConfigurationUserProfileRepository configurationUserProfileRepository;

    @PostMapping("/user")
    public String create(@RequestBody Map<String, Object> data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();

        try {
            for (String element : data.keySet()) {
                System.out.println(element);
            }

            CatalogUserEntity systemUserEntity = systemUserService.createUser(data);

            if(systemUserEntity == null) {
                throw new Exception("Ocurrió un error al registrar el usuario");
            } else {
                resultMap.put("status", 1);
                resultMap.put("message", "Petición Correcta");
                resultMap.put("data", systemUserEntity.toMap());
                
                jsonResponse = mapper.writeValueAsString(resultMap);
            }

        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error on SystemUserController::create[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }
        
        return jsonResponse;
    }
    

    @GetMapping("/user/current")
    public String current(Authentication authentication) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();
        CatalogUserEntity systemUserEntity = null;

        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            systemUserEntity = systemUserService.getSystemUserEntityByNickname(userDetails.getUsername());

            resultMap.put("status", 1);
            resultMap.put("message", "Petición Correcta");
            resultMap.put("data", systemUserEntity.toMap());
            
            jsonResponse = mapper.writeValueAsString(resultMap);

        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error on SystemUserController::create[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }

        return jsonResponse;
    }
    
    @PostMapping("/user/assign-profiles")
    public String assignProfiles(@RequestBody Map<String, Object> data, Authentication authentication) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();
        Long userId;

        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            CatalogUserEntity systemUserEntity = userEntityRepository.findByNickname(userDetails.getUsername());

            userId = Long.parseLong(data.get("userId").toString());
            Optional<CatalogUserEntity> userEntityResult = userEntityRepository.findById(userId);
            
            if (!userEntityResult.isPresent()) {
                throw new Exception("El usuario no existe");
            }

            CatalogUserEntity userEntity = userEntityResult.get();

            List<Long> profileIds = (ArrayList<Long>) data.get("profileIds");
            //systemUserService.addProfilesToUser(data);
            if (profileIds.size() > 0) {
                List<CatalogProfileEntity> profileEntityList = profileRepository.findAllById(profileIds);
                
                for (CatalogProfileEntity profileEntity : profileEntityList) {
                    ConfigurationUserProfileEntity configurationUserProfileEntity = new ConfigurationUserProfileEntity();
                    configurationUserProfileEntity.setUserEntity(userEntity);
                    configurationUserProfileEntity.setProfileEntity(profileEntity);
                    configurationUserProfileEntity.setActive(1);
                    configurationUserProfileEntity.setAssignedAt(LocalDateTime.now());
                    configurationUserProfileEntity.setAssignedBy(systemUserEntity);
                    configurationUserProfileEntity.setUnassignedBy(systemUserEntity);

                    configurationUserProfileRepository.saveAndFlush(configurationUserProfileEntity);
                }
            }

            resultMap.put("status", 1);
            resultMap.put("message", "Perfiles asignados correctamente");
            resultMap.put("data", null);
            
            jsonResponse = mapper.writeValueAsString(resultMap);

        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error al asignar perfiles al usuario: " + e.getLocalizedMessage());
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }

        return jsonResponse;
    }

    @GetMapping("/user/unique-email")
    public String uniqueEmail(@RequestParam String email) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> resultMap = new HashMap<>();

        try {
            Optional<CatalogUserEntity> userEntityResult = userEntityRepository.findOneByEmail(email);
            
            resultMap.put("status", 1);
            resultMap.put("message", "Petición Correcta");
            resultMap.put("data", !userEntityResult.isPresent() ? 1 : 2);
        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error al validar email único: " + e.getLocalizedMessage());
            resultMap.put("data", false);
        }
        
        return mapper.writeValueAsString(resultMap);
    }
}
