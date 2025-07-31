package com.bcb.trust.front.modules.configuration.controller.front;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bcb.trust.front.modules.configuration.model.entity.ConfigurationEndpointEntity;
import com.bcb.trust.front.modules.configuration.model.repository.ConfigurationEndpointRepository;
import com.bcb.trust.front.modules.system.model.entity.SystemProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemUserEntityRepository;

@Controller
@RequestMapping("/configuration")
public class ConfigurationEndpointController {

    @Autowired
    private ConfigurationEndpointRepository endpointRepository;

    @Autowired
    private SystemUserEntityRepository systemUserEntityRepository;

    @GetMapping("/endpoint")
    public String get(Authentication authentication, Model model) {

        List<ConfigurationEndpointEntity> configurationEndpointEntityList = new ArrayList<>();

        try {
            configurationEndpointEntityList = endpointRepository.findAll();

        } catch (Exception e) {
            // TODO: handle exception
        }

        model.addAttribute("configurationEndpointEntityList", configurationEndpointEntityList);

        return "thirdparty/endpoint/index";
    }

    @GetMapping("/endpoint/create")
    public String getMethodName(@RequestParam String param) {
        return "thirdparty/endpoint/create";
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
