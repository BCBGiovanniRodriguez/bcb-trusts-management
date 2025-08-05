package com.bcb.trust.front.modules.system.controller.front;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bcb.trust.front.modules.common.model.CommonEntity;
import com.bcb.trust.front.modules.system.model.entity.SystemPermissionEntity;
import com.bcb.trust.front.modules.system.model.entity.SystemProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemPermissionRepository;
import com.bcb.trust.front.modules.system.model.repository.SystemProfileRepository;
import com.bcb.trust.front.modules.system.model.repository.SystemUserEntityRepository;

@Controller
@RequestMapping("/system")
public class ProfileController {

    @Autowired
    private SystemUserEntityRepository systemUserEntityRepository;

    @Autowired
    private SystemProfileRepository systemProfileRepository;

    @Autowired
    private SystemPermissionRepository systemPermissionRepository;

    @GetMapping("/profile")
    public String index(@RequestParam(required = false) Integer status, Model model) {
        List<SystemProfileEntity> profileEntityList = null;
        String[] statuses = null;

        try {
            statuses = CommonEntity.statuses;

            if (status != null) {
                profileEntityList = systemProfileRepository.findByStatus(status);
            } else if (status == null) {
                status = 0;
            }
            
        } catch (Exception e) {
            System.out.println("" + e.getLocalizedMessage());
        }

        model.addAttribute("statusQueryParam", (Integer) status);
        model.addAttribute("statuses", statuses);
        model.addAttribute("profileEntityList", profileEntityList);

        return "system/profile/index";
    }

    @GetMapping("/profile/create")
    public String create() {

        try {
            
        } catch (Exception e) {
            
        }

        return "system/profile/create";
    }

    @GetMapping("/profile/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        SystemProfileEntity systemProfileEntity = null;
        Set<SystemPermissionEntity> systemProfilePermissionEntityList = null;
        List<SystemPermissionEntity> systemPermissionList = new ArrayList<>();
        try {
            systemPermissionList = systemPermissionRepository.findAll();
            Optional<SystemProfileEntity> result = systemProfileRepository.findById(id);
            
            if (!result.isPresent()) {
                throw new Exception("");
            } else {
                systemProfileEntity = result.get();
                systemProfilePermissionEntityList = systemProfileEntity.getPermissions();
                for (SystemPermissionEntity systemPermissionEntity : systemProfilePermissionEntityList) {
                    System.out.println("PermisoDelPerfil:" + systemPermissionEntity.getName() );
                }

                for (SystemPermissionEntity systemPermissionEntity : systemPermissionList) {
                    if (systemProfilePermissionEntityList.contains(systemPermissionEntity)) {
                        System.out.println("Contenido en lista principal: " + systemPermissionEntity.getName());
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        model.addAttribute("systemProfileEntity", systemProfileEntity);
        model.addAttribute("systemProfilePermissionEntityList", systemProfilePermissionEntityList);
        model.addAttribute("systemPermissionList", systemPermissionList);

        return "system/profile/update";
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
