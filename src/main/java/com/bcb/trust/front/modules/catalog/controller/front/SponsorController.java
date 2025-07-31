package com.bcb.trust.front.modules.catalog.controller.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bcb.trust.front.modules.system.model.entity.SystemProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemUserEntityRepository;

@Controller
@RequestMapping("/catalog/sponsor")
public class SponsorController {

    @Autowired
    private SystemUserEntityRepository systemUserEntityRepository;

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


    @ModelAttribute("systemUserEntity")
    public SystemUserEntity systemUserEntity(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        SystemUserEntity systemUserEntity = systemUserEntityRepository.findByNickname(userDetails.getUsername());
        return systemUserEntity;
    }

    @ModelAttribute("systemProfileEntity")
    public SystemProfileEntity systemProfileEntity(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        SystemUserEntity systemUserEntity = systemUserEntityRepository.findByNickname(userDetails.getUsername());
        return systemUserEntity.getProfile();
    }
}
