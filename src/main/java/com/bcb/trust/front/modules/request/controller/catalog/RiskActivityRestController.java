package com.bcb.trust.front.modules.request.controller.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcb.trust.front.modules.request.model.entity.catalog.RiskActivityEntity;
import com.bcb.trust.front.modules.request.model.repository.catalog.RiskActivityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/request/catalog")
public class RiskActivityRestController {

    @Autowired
    private RiskActivityRepository riskActivityRepository;

    @GetMapping("/risk-activity")
    public String getAll() {

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = null;
        List<Map<String, Object>> resultList = new ArrayList<>();

        try {
            List<RiskActivityEntity> riskActivityEntities = riskActivityRepository.findAll();

            for (RiskActivityEntity riskActivityEntity : riskActivityEntities) {
                resultList.add(riskActivityEntity.toMap());
            }

            jsonResponse = mapper.writeValueAsString(resultList);
        } catch (Exception e) {
            // TODO: handle exception
        }
        
        return jsonResponse;
    }
    
}
