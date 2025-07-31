package com.bcb.trust.front.modules.request.controller.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.bcb.trust.front.modules.system.model.entity.SystemProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemUserEntityRepository;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/request")
public class RequestController {

    @Autowired
    private SystemUserEntityRepository systemUserEntityRepository;

    @GetMapping("/request")
    public String queryForm(@RequestParam(required = false) String param) {
        return "request/request/index";
    }

    @PostMapping("/request")
    public String querySubmit(@RequestBody(required = false) String entity) {
        //TODO: process POST request
        
        return "request/request/index";
    }
    

    @GetMapping("/request/create")
    public String requestCreateForm(@RequestParam(required = false) String param) {
        return "request/request/create";
    }

    @PostMapping("/request/create")
    public String requestCreateSubmit(@RequestBody(required = false) String entity) {
        
        return "request/request/create";
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
