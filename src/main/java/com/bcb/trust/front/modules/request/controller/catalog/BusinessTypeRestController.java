package com.bcb.trust.front.modules.request.controller.catalog;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcb.trust.front.modules.request.model.entity.catalog.BusinessTypeEntity;
import com.bcb.trust.front.modules.request.model.repository.catalog.BusinessTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/request/catalog")
public class BusinessTypeRestController {

    @Autowired
    private BusinessTypeRepository businessTypeRepository;

    @GetMapping("/business-types")
    public String get(@RequestParam(required = false) String param) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = null;
        List<Map<String, Object>> resultList = new ArrayList<>();

        try {
            List<BusinessTypeEntity> businessTypeList = businessTypeRepository.findAll();

            for (BusinessTypeEntity businessTypeEntity : businessTypeList) {
                resultList.add(businessTypeEntity.toMap());
            }

            jsonResponse = mapper.writeValueAsString(resultList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }

}
