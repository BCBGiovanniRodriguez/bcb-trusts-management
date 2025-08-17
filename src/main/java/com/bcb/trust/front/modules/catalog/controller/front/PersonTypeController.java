package com.bcb.trust.front.modules.catalog.controller.front;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bcb.trust.front.model.trusts.enums.StatusEnum;
import com.bcb.trust.front.modules.catalog.model.entity.CatalogPersonTypeEntity;
import com.bcb.trust.front.modules.catalog.model.repository.CatalogPersonTypeRepository;

@Controller
@RequestMapping("/catalog/person-type")
public class PersonTypeController {

    @Autowired
    private CatalogPersonTypeRepository personTypeRepository;

    @RequestMapping("/")
    public String index(Model model) {
        List<CatalogPersonTypeEntity> personTypeList = new ArrayList<>();

        try {
            StatusEnum status = StatusEnum.ENABLED;
            personTypeList = personTypeRepository.findByStatus(status);

        } catch (Exception e) {
            System.out.println("Error on PersonTypeController::index " + e.getLocalizedMessage());
        }

        model.addAttribute("personTypeList", personTypeList);

        return "catalog/person-type/index";
    }

}
