package com.bcb.trust.front.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemUserEntityRepository;

@Controller
public class IndexController {

    @Autowired
    private SystemUserEntityRepository systemUserEntityRepository;

    @GetMapping("/")
    public String welcome(Authentication authentication, Model model) {
        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            SystemUserEntity systemUserEntity = systemUserEntityRepository.findByNickname(userDetails.getUsername());

            model.addAttribute("systemUserEntity", systemUserEntity);
            model.addAttribute("systemProfileEntity", systemUserEntity.getProfile());
        }
        
        return "welcome";
    }
    
    @GetMapping("/landing")
    public String landingForm(@RequestParam(required = false) String param) {
        return "landingpage";
    }
}
