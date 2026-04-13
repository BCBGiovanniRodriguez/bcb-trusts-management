package com.bcb.trust.front.modules.request.controller.catalog.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bcb.trust.front.modules.request.model.entity.catalog.BusinessTypeEntity;
import com.bcb.trust.front.modules.request.model.repository.catalog.BusinessTypeRepository;


@Controller
@RequestMapping("/request/catalog")
public class BusinesTypeFrontController {

    @Autowired
    private BusinessTypeRepository businessTypeRepository;

    @GetMapping("/business-type-front")
    public String index(Model model) {

        List<BusinessTypeEntity> businessTypes = businessTypeRepository.findAll();

        model.addAttribute("businessTypeList", businessTypes);

        return "request/catalog/business-type/index";
    }

}
