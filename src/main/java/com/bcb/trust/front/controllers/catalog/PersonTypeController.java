package com.bcb.trust.front.controllers.catalog;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bcb.trust.front.model.trusts.enums.StatusEnum;
import com.bcb.trust.front.model.trusts.entity.catalog.PersonType;
import com.bcb.trust.front.model.trusts.repository.catalog.PersonTypeRepository;

@Controller
@RequestMapping("/catalog/person-type")
public class PersonTypeController {

    @Autowired
    private PersonTypeRepository personTypeRepository;

    @RequestMapping("/")
    public String index(Model model) {
        List<PersonType> personTypeList = new ArrayList<>();

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
