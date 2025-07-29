package com.bcb.trust.front.controllers.catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bcb.trust.front.model.trusts.entity.catalog.Person;
import com.bcb.trust.front.modules.catalog.model.entity.CatalogAddress;
import com.bcb.trust.front.modules.catalog.model.repository.CatalogConsultantRepository;

@Controller
@RequestMapping("/catalog/consultant")
public class ConsultantController {

    @Autowired
    private CatalogConsultantRepository consultantRepository;

    @GetMapping("/")
    public String index() {

        return "catalog/consultant/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        Person person = new Person();
        CatalogAddress address = new CatalogAddress();

        model.addAttribute("person", person);
        model.addAttribute("address", address);
        
        return "catalog/consultant/create";
    }

    @PostMapping("/create")
    public String createSubmit(Model model) {
        
        
        return "catalog/consultant/create";
    }
    

    
}
