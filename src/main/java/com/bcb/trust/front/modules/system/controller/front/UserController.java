package com.bcb.trust.front.modules.system.controller.front;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bcb.trust.front.modules.common.model.CommonEntity;
import com.bcb.trust.front.modules.system.model.entity.CatalogProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.CatalogUserEntity;
import com.bcb.trust.front.modules.system.model.entity.ConfigurationUserProfileEntity;
import com.bcb.trust.front.modules.system.model.repository.ConfigurationUserProfileRepository;
import com.bcb.trust.front.modules.system.model.repository.ProfileRepository;
import com.bcb.trust.front.modules.system.model.repository.UserEntityRepository;

@Controller
@RequestMapping("/system")
public class UserController {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ConfigurationUserProfileRepository configurationUserProfileRepository;

    @GetMapping("/user")
    public String index(@RequestParam(required = false) Integer status, Model model) {
        List<CatalogUserEntity> userEntityList = null;
        List<CatalogProfileEntity> profileEntityList = null;

        try {
            if (status != null) {
                userEntityList = userEntityRepository.findByStatus(status);
            }

            profileEntityList = profileRepository.findAll();
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
        List<CatalogProfileEntity> profileEntityList = null;

        try {
            profileEntityList = profileRepository.findAll();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        model.addAttribute("profileEntityList", profileEntityList);
        return "system/user/create";
    }

    @GetMapping("/user/detail/{id}")
    public String detail(@NonNull @PathVariable Long id, Model model) {
        CatalogUserEntity userEntity = null;
        List<CatalogProfileEntity> profileEntityList = new ArrayList<>();
        List<CatalogProfileEntity> availableProfileEntityList = new ArrayList<>();
        List<ConfigurationUserProfileEntity> assignedConfigurationUserProfileList = new ArrayList<>();

        try {
            profileEntityList = profileRepository.findAll();
            Optional<CatalogUserEntity> result = userEntityRepository.findById(id);
            
            if (result.isPresent()) {
                userEntity = result.get();
                assignedConfigurationUserProfileList = configurationUserProfileRepository.findByUserEntity(userEntity);

                for (CatalogProfileEntity profileEntity : profileEntityList) {
                    CatalogProfileEntity profileEntityTmp = assignedConfigurationUserProfileList.stream()
                            .filter(assignedProfile -> assignedProfile.getProfileEntity().getProfileId().equals(profileEntity.getProfileId()))
                            .map(ConfigurationUserProfileEntity::getProfileEntity)
                            .findFirst()
                            .orElse(null);

                    if (profileEntityTmp == null) {
                        availableProfileEntityList.add(profileEntity);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        
        model.addAttribute("userEntity", userEntity);
        model.addAttribute("profileEntityList", profileEntityList);
        model.addAttribute("availableProfileEntityList", availableProfileEntityList);
        model.addAttribute("assignedConfigurationUserProfileList", assignedConfigurationUserProfileList);

        return "system/user/detail";
    }

}
