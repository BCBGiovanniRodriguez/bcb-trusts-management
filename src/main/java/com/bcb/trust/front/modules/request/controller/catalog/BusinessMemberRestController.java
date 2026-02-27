package com.bcb.trust.front.modules.request.controller.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcb.trust.front.modules.request.model.repository.catalog.BusinessMemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/request/catalog")
public class BusinessMemberRestController {

    @Autowired
    private BusinessMemberRepository businessMemberRepository;
    
    @GetMapping("/business-member")
    public String get() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = null;
        List<Map<String, Object>> resultList = new ArrayList<>();

        try {
            businessMemberRepository.findAll().forEach(businessMemberEntity -> {
                resultList.add(businessMemberEntity.toMap());
            });
            
            jsonResponse = mapper.writeValueAsString(resultList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }
    
}
