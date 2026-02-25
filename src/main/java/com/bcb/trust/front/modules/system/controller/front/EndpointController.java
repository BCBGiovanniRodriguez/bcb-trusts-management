package com.bcb.trust.front.modules.system.controller.front;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bcb.trust.front.modules.system.model.entity.CatalogEndpointEntity;
import com.bcb.trust.front.modules.system.model.repository.CatalogEndpointRepository;

@Controller
@RequestMapping("/system/configuration")
public class EndpointController {

    @Autowired
    private CatalogEndpointRepository endpointRepository;

    @GetMapping("/endpoint")
    public String get(Authentication authentication, Model model) {

        List<CatalogEndpointEntity> configurationEndpointEntityList = new ArrayList<>();

        try {
            configurationEndpointEntityList = endpointRepository.findAll();

        } catch (Exception e) {
            // TODO: handle exception
        }

        model.addAttribute("configurationEndpointEntityList", configurationEndpointEntityList);
        
        return "system/endpoint/index";
    }
    
    @GetMapping("/endpoint/create")
    public String createGet(Authentication authentication, Model model) {
        model.addAttribute("types", CatalogEndpointEntity.typeNames);
        model.addAttribute("envs", CatalogEndpointEntity.envNames);
        model.addAttribute("codes", CatalogEndpointEntity.codeNames);
        
        return "system/endpoint/create";
    }

    @GetMapping("/endpoint/update/{id}")
    public String updateGet(@PathVariable Long id, Model model) {
        CatalogEndpointEntity endpoint = endpointRepository.findById(id).orElse(null);
        
        if (endpoint != null) {
            model.addAttribute("endpoint", endpoint);
        }
        
        model.addAttribute("types", CatalogEndpointEntity.typeNames);
        model.addAttribute("envs", CatalogEndpointEntity.envNames);
        model.addAttribute("codes", CatalogEndpointEntity.codeNames);

        return "system/endpoint/update";
    }
    

}
