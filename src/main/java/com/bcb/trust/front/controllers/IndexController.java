package com.bcb.trust.front.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class IndexController {

    @GetMapping("/")
    public String welcome() {
        
        return "welcome";
    }
    
    @GetMapping("/landing")
    public String landingForm(@RequestParam(required = false) String param) {
        return "landingpage";
    }
    
}
