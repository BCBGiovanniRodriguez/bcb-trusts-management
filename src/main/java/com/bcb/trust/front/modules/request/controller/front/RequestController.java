package com.bcb.trust.front.modules.request.controller.front;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bcb.trust.front.model.trusts.enums.StatusEnum;
import com.bcb.trust.front.modules.request.model.entity.RequestRequestEntity;
import com.bcb.trust.front.modules.request.model.repository.RequestEntityRepository;
import com.bcb.trust.front.modules.system.model.entity.SystemProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemUserEntityRepository;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustTypeEntity;
import com.bcb.trust.front.modules.trust.model.repository.TrustTrustTypeRepository;

@Controller
@RequestMapping("/request")
public class RequestController {

    @Autowired
    private SystemUserEntityRepository systemUserEntityRepository;

    @Autowired
    private TrustTrustTypeRepository trustTypeRepository;

    @Autowired
    private RequestEntityRepository requestEntityRepository;

    @GetMapping("/request")
    public String queryForm(@RequestParam(required = false) String param, Model model) {
        List<RequestRequestEntity> requestList = new ArrayList<>();
        try {
            requestList = requestEntityRepository.findAll();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        model.addAttribute("requestList", requestList);

        return "request/request/index";
    }

    @GetMapping("/request/detail/{id}")
    public String detail(@PathVariable Long id) {
        
        return "request/request/detail";
    }
    

    @PostMapping("/request")
    public String querySubmit(@RequestBody(required = false) String entity) {
        //TODO: process POST request
        
        return "request/request/index";
    }
    

    @GetMapping("/request/create")
    public String requestCreateForm(@RequestParam(required = false) String param, Model model) {
        List<TrustTrustTypeEntity> trustTypeEntityList = new ArrayList<>();

        try {
            trustTypeEntityList = trustTypeRepository.findByStatus(StatusEnum.ENABLED);

        } catch (Exception e) {
            System.out.println("Error: " + e.getLocalizedMessage());
        }

        model.addAttribute("trustTypeEntityList", trustTypeEntityList);

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
