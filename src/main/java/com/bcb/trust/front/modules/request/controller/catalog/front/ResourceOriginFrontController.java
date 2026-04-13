package com.bcb.trust.front.modules.request.controller.catalog.front;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bcb.trust.front.modules.request.model.entity.catalog.ResourceOriginEntity;
import com.bcb.trust.front.modules.request.model.repository.catalog.ResourceOriginRepository;


@Controller
@RequestMapping("/request/catalog")
public class ResourceOriginFrontController {

    @Autowired
    private ResourceOriginRepository resourceOriginRepository;

    @GetMapping("/resource-origin-front")
    public String index(Model model) {

        List<ResourceOriginEntity> resourceOrigins = new ArrayList<>();

        try {
            resourceOrigins = resourceOriginRepository.findAll();
        } catch (Exception e) {
            // TODO: handle exception
        }

        model.addAttribute("resourceOrigins", resourceOrigins);

        return "request/catalog/resource-origin/index";
    }
    
}
