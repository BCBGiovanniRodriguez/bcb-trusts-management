package com.bcb.trust.front.modules.system.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;
import com.bcb.trust.front.modules.system.service.SystemUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@RestController
@RequestMapping("/api/system")
public class SystemUserController {

    @Autowired
    private SystemUserService systemUserService;

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

            SystemUserEntity systemUserEntity = systemUserService.createUser(data);

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
        SystemUserEntity systemUserEntity = null;

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
    
}
