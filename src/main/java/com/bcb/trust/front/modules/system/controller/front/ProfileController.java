package com.bcb.trust.front.modules.system.controller.front;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bcb.trust.front.modules.common.model.CommonEntity;
import com.bcb.trust.front.modules.system.model.entity.SystemResourceEntity;
import com.bcb.trust.front.modules.system.model.entity.ConfigurationProfileResourceEntity;
import com.bcb.trust.front.modules.system.model.entity.SystemProfileEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemResourceRepository;
import com.bcb.trust.front.modules.system.model.repository.SystemProfileRepository;

@Controller
@RequestMapping("/system")
public class ProfileController {

    @Autowired
    private SystemProfileRepository systemProfileRepository;

    @Autowired
    private SystemResourceRepository systemResourceRepository;

    @GetMapping("/profile")
    public String index(@RequestParam(required = false) Integer status, Model model) {
        List<SystemProfileEntity> profileEntityList = new ArrayList<>();
        String[] statuses = null;

        try {
            statuses = CommonEntity.statuses;

            if (status != null) {
                // profileEntityList = systemProfileRepository.findByStatus(status);
                profileEntityList = systemProfileRepository.findAll();
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
        List<ConfigurationProfileResourceEntity> profileResourceList = new ArrayList<>();

        Set<SystemResourceEntity> systemProfileResourceEntityList = new HashSet<>();
        List<SystemResourceEntity> systemResourceList = new ArrayList<>();
        String resultMessage = "";
        Integer resultStatus = 0;

        try {
            systemResourceList = systemResourceRepository.findAll();
            Optional<SystemProfileEntity> result = systemProfileRepository.findById(id);

            if (!result.isPresent()) {
                resultMessage = "Perfil no encontrado";
                resultStatus = 0;
            } else {
                resultStatus = 1;
                systemProfileEntity = result.get();
                resultMessage = "Perfil encontrado";

                profileResourceList = systemProfileEntity.getProfileResourceList();
                /*
                 * systemProfileResourceEntityList = systemProfileEntity.getResources();
                 * for (SystemResourceEntity systemResourceEntity :
                 * systemProfileResourceEntityList) {
                 * System.out.println("RecursoDelPerfil:" + systemResourceEntity.getName() );
                 * }
                 * 
                 * for (SystemResourceEntity systemResourceEntity : systemResourceList) {
                 * if (systemProfileResourceEntityList.contains(systemResourceEntity)) {
                 * System.out.println("Contenido en lista principal: " +
                 * systemResourceEntity.getName());
                 * }
                 * }
                 */
            }

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        model.addAttribute("resultStatus", resultStatus);
        model.addAttribute("resultMessage", resultMessage);

        model.addAttribute("profileResourceList", profileResourceList);
        model.addAttribute("systemProfileEntity", systemProfileEntity);
        model.addAttribute("systemProfileResourceEntityList", systemProfileResourceEntityList);
        model.addAttribute("systemResourceList", systemResourceList);

        return "system/profile/update";
    }

}
