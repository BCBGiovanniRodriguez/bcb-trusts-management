package com.bcb.trust.front.modules.system.controller.front;

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

import com.bcb.trust.front.modules.system.model.entity.SystemPermissionEntity;
import com.bcb.trust.front.modules.system.model.entity.SystemProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemPermissionRepository;
import com.bcb.trust.front.modules.system.model.repository.SystemUserEntityRepository;

@Controller
@RequestMapping("/system")
public class PermissionController {

    @Autowired
    private SystemUserEntityRepository systemUserEntityRepository;

    @Autowired
    private SystemPermissionRepository systemPermissionRepository;

    @GetMapping("/permission")
    public String list(@RequestParam(required = false) Integer module, @RequestParam(required = false) Integer status, Model model) {
        List<SystemPermissionEntity> permissionList = new ArrayList<>();
        String[] moduleNames = null;
        String[] statuses = null;

        try {
            moduleNames = SystemPermissionEntity.moduleNames;
            statuses = SystemPermissionEntity.statuses;

            if (module != null && status != null) {
                permissionList = systemPermissionRepository.findByModuleAndStatus(module, status);
            } else if(module == null && status != null) {
                permissionList = systemPermissionRepository.findByStatus(status);
            } else if (module != null && status == null) {
                permissionList = systemPermissionRepository.findByModule(module);
            }

            if (status == null) {
                status = 0;
            }

            if (module == null) {
                module = 0;
            }

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        model.addAttribute("moduleQueryParam", (Integer) module);
        model.addAttribute("statusQueryParam", (Integer) status);
        model.addAttribute("statuses", statuses);
        model.addAttribute("moduleNames", moduleNames);
        model.addAttribute("permissionList", permissionList);

        return "system/permission/index";
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
