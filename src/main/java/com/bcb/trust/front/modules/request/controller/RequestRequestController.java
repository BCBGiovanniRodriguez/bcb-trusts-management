package com.bcb.trust.front.modules.request.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcb.trust.front.modules.request.model.entity.RequestRequestEntity;
import com.bcb.trust.front.modules.request.service.RequestRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@RestController
@RequestMapping("/api/request")
public class RequestRequestController {

    @Autowired
    private RequestRequestService requestRequestService;

    @PostMapping("/request")
    public String create(@RequestBody Map<String, Object> data, Authentication authentication) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();
        RequestRequestEntity requestEntity = null;

        try {
            requestEntity = requestRequestService.saveRequest(data, authentication);
            
            resultMap.put("status", 1);
            resultMap.put("message", "Petición Correcta");
            resultMap.put("data", requestEntity.toMap());
            
            jsonResponse = mapper.writeValueAsString(resultMap);
            
        } catch (Exception e) {
            System.out.println("Error en RequestRequestController::create[" + e.getLocalizedMessage() + "]");
            resultMap.put("status", 0);
            resultMap.put("message", "Error en RequestRequestController::create[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }
        
        return jsonResponse;
    }
    
}
