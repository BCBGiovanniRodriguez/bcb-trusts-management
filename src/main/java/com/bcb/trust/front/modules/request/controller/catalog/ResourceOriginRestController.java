package com.bcb.trust.front.modules.request.controller.catalog;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcb.trust.front.modules.request.model.entity.catalog.ResourceOriginEntity;
import com.bcb.trust.front.modules.request.model.repository.catalog.ResourceOriginRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/request/catalog")
public class ResourceOriginRestController {

    @Autowired
    private ResourceOriginRepository resourceOriginRepository;

    @GetMapping("/resource-origin")
    public String getAll() {

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = null;
        List<Map<String, Object>> resultList = new ArrayList<>();

        try {
            List<ResourceOriginEntity> resourceOriginEntities = resourceOriginRepository.findAll();
            for (ResourceOriginEntity resourceOriginEntity : resourceOriginEntities) {
                resultList.add(resourceOriginEntity.toMap());
            }

            jsonResponse = mapper.writeValueAsString(resultList);
        } catch (Exception e) {
            // TODO: handle exception
        }

        return jsonResponse;
    }
    
}
