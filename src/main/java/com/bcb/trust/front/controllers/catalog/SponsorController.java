package com.bcb.trust.front.controllers.catalog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/catalog/sponsor")
public class SponsorController {

    @GetMapping("/")
    public String index(Model model) {

        return "catalog/sponsor/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        

        model.addAttribute("", model);
        return "catalog/sponsor/create";
    }

    @PostMapping("/create")
    public String createSubmit(Model model) {
        
        
        return "catalog/sponsor/create";
    }

}
