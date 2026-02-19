package com.bcb.trust.front.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @GetMapping("/")
    public String welcome(Authentication authentication, Model model) {
        return "welcome";
    }

    @GetMapping("/landing")
    public String landingForm(@RequestParam(required = false) String param) {
        return "landingpage";
    }
}
