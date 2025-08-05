package com.bcb.trust.front.modules.system.controller.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.bcb.trust.front.modules.common.model.CommonEntity;
import com.bcb.trust.front.modules.system.model.entity.SystemProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemProfileRepository;
import com.bcb.trust.front.modules.system.model.repository.SystemUserEntityRepository;


@Controller
@RequestMapping("/system")
public class UserController {

    @Autowired
    private SystemUserEntityRepository systemUserEntityRepository;

    @Autowired
    private SystemProfileRepository systemProfileRepository;

    @GetMapping("/user")
    public String index(@RequestParam(required = false) Integer status, Model model) {
        List<SystemUserEntity> userEntityList = null;
        List<SystemProfileEntity> profileEntityList = null;

        try {
            if (status != null) {
                userEntityList = systemUserEntityRepository.findByStatus(status);
            }

            profileEntityList = systemProfileRepository.findByStatus(1);
        } catch (Exception e) {
            System.out.println("" + e.getLocalizedMessage());
        }

        model.addAttribute("statuses", CommonEntity.statuses);
        model.addAttribute("statusQueryParam", status);
        model.addAttribute("profileEntityList", profileEntityList);
        model.addAttribute("userEntityList", userEntityList);

        return "system/user/index";
    }

    @GetMapping("/user/create")
    public String create(Model model) {
        List<SystemProfileEntity> profileEntityList = null;

        try {
            profileEntityList = systemProfileRepository.findByStatus(1);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        model.addAttribute("profileEntityList", profileEntityList);
        return "system/user/create";
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
