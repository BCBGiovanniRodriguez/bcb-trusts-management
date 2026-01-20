package com.bcb.trust.front.modules.system.controller.front;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bcb.trust.front.modules.system.model.entity.CatalogEndpointEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemCatalogEndpointRepository;

@Controller
@RequestMapping("/configuration")
public class ConfigurationEndpointController {

    @Autowired
    private SystemCatalogEndpointRepository endpointRepository;

    @GetMapping("/endpoint")
    public String get(Authentication authentication, Model model) {

        List<CatalogEndpointEntity> configurationEndpointEntityList = new ArrayList<>();

        try {
            configurationEndpointEntityList = endpointRepository.findAll();

        } catch (Exception e) {
            // TODO: handle exception
        }

        model.addAttribute("configurationEndpointEntityList", configurationEndpointEntityList);

        return "thirdparty/endpoint/index";
    }

    @GetMapping("/endpoint/create")
    public String getMethodName(@RequestParam String param) {
        return "thirdparty/endpoint/create";
    }

}
