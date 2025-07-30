package com.bcb.trust.front.modules.catalog.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcb.trust.front.modules.catalog.model.entity.CatalogConsultantEntity;
import com.bcb.trust.front.modules.catalog.model.repository.CatalogConsultantRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@RequestMapping("/api/catalog")
public class CatalogConsultantController {

    @Autowired
    private CatalogConsultantRepository consultantRepository;

    @PostMapping("/consultant")
    public String create(@RequestBody CatalogConsultantEntity consultant) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();

        try {

            consultantRepository.saveAndFlush(consultant);
            
            resultMap.put("status", 1);
            resultMap.put("message", "Petición Correcta");
            resultMap.put("data", consultant);

            jsonResponse = mapper.writeValueAsString(resultMap);
        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error en: CatalogConsultantController::post[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }
        
        return jsonResponse;
    }

    @PutMapping("/consultant/{id}")
    public String putMethodName(@PathVariable String id, @RequestBody CatalogConsultantEntity consultant) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();
        
        try {
            Optional<CatalogConsultantEntity> optional = consultantRepository.findById(Long.parseLong(id));

            if (optional.isPresent()) {
                optional.get()
                    .setStatus(consultant.getStatus());
                
                consultantRepository.flush();
            } else {
                throw new Exception("Registro con identificador '" + id + "'' no encontrado");
            }

            resultMap.put("status", 1);
            resultMap.put("message", "Petición Correcta");
            resultMap.put("data", consultant);

            jsonResponse = mapper.writeValueAsString(resultMap);
        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error en: CatalogConsultantController::post[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }
        
        return jsonResponse;
    }
    
}
