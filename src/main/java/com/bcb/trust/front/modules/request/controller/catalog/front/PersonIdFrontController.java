package com.bcb.trust.front.modules.request.controller.catalog.front;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bcb.trust.front.modules.catalog.model.repository.CatalogPersonEntityRepository;
import com.bcb.trust.front.modules.system.model.entity.CatalogPersonEntity;


@Controller
@RequestMapping("/request")
public class PersonIdFrontController {

    @Autowired
    private CatalogPersonEntityRepository catalogPersonEntityRepository;

    @GetMapping("/person-id-front")
    public String index(@PathVariable(required = false) String name, @PathVariable(required = false) String rfc, Model model) {
        List<CatalogPersonEntity> personList = new ArrayList<>(); // Aquí deberías llamar a tu servicio para obtener los IDs de personas según el nombre y RFC
        List<CatalogPersonEntity> resultList = new ArrayList<>();

        try {
            if (name != null && name != "") {
                personList.addAll(catalogPersonEntityRepository.findByFirstNameStartsWith(name));
                personList.addAll(catalogPersonEntityRepository.findBySecondNameStartsWith(name));
                personList.addAll(catalogPersonEntityRepository.findByLastNameStartsWith(name));
                personList.addAll(catalogPersonEntityRepository.findBySecondLastNameStartsWith(name));
            }

            if (rfc != null && rfc != "") {
                personList.addAll(catalogPersonEntityRepository.findByRfcStartsWith(rfc));
            }

            // Added, now filter by id
            for (CatalogPersonEntity person : personList) {
                if (!resultList.contains(person)) {
                    resultList.add(person);
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }

        model.addAttribute("personList", personList);

        return "request/person-id/index";
    }
    

}
