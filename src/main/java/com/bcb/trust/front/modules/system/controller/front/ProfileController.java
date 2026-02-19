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

import com.bcb.trust.front.modules.system.model.entity.CatalogProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.CatalogResourceEntity;
import com.bcb.trust.front.modules.system.model.entity.ConfigurationProfileResourceEntity;
import com.bcb.trust.front.modules.system.model.repository.ProfileRepository;
import com.bcb.trust.front.modules.system.model.repository.ProfileResourceRepository;
import com.bcb.trust.front.modules.system.model.repository.ResourceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Controller
@RequestMapping("/system")
public class ProfileController {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ProfileResourceRepository profileResourceRepository;

    @GetMapping("/profile")
    public String index(Model model) {
        List<CatalogProfileEntity> profileEntityList = new ArrayList<>();

        try {
            profileEntityList = profileRepository.findAll();
        } catch (Exception e) {
            System.out.println("" + e.getLocalizedMessage());
        }

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
        CatalogProfileEntity profileEntity = null;
        List<ConfigurationProfileResourceEntity> assignedConfigurationProfileResourceList = new ArrayList<>();
        List<CatalogResourceEntity> resourceAvailableList = new ArrayList<>();

        Set<CatalogResourceEntity> profileResourceEntityList = new HashSet<>();
        List<CatalogResourceEntity> resourceEntityList = new ArrayList<>();
        String resultMessage = "";
        Integer resultStatus = 0;
        String availableResourcesJson = null;

        List<CatalogResourceEntity> undefinedAvailableResourceList = new ArrayList<>();
        List<CatalogResourceEntity> systemAvailableResourceList = new ArrayList<>();
        List<CatalogResourceEntity> requestAvailableResourceList = new ArrayList<>();
        List<CatalogResourceEntity> adminAvailableResourceList = new ArrayList<>();
        List<CatalogResourceEntity> operationAvailableResourceList = new ArrayList<>();
        List<CatalogResourceEntity> accountingAvailableResourceList = new ArrayList<>();
        List<CatalogResourceEntity> reportAvailableResourceList = new ArrayList<>();
        List<CatalogResourceEntity> pldAvailableResourceList = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            resourceEntityList = resourceRepository.findAll();
            Optional<CatalogProfileEntity> result = profileRepository.findById(id);

            if (!result.isPresent()) {
                resultMessage = "Perfil no encontrado";
                resultStatus = 0;
            } else {
                resultStatus = 1;
                profileEntity = result.get();
                resultMessage = "Perfil encontrado";

                assignedConfigurationProfileResourceList = profileEntity.getConfigurationProfileResourceList();

                for (CatalogResourceEntity resourceEntity : resourceEntityList) {
                    ConfigurationProfileResourceEntity cpr = assignedConfigurationProfileResourceList.stream()
                        .filter(cprEntity -> cprEntity.getResourceEntity().getResourceId() == resourceEntity.getResourceId())
                        .findAny()
                        .orElse(null);

                    if (cpr == null) {
                        resourceAvailableList.add(resourceEntity);

                        switch (resourceEntity.getModule()) {
                            case 0:
                                undefinedAvailableResourceList.add(resourceEntity);
                                break;
                            case 1:
                                systemAvailableResourceList.add(resourceEntity);
                                break;
                            case 2:
                                requestAvailableResourceList.add(resourceEntity);
                                break;
                            case 3:
                                adminAvailableResourceList.add(resourceEntity);
                                break;
                            case 4:
                                operationAvailableResourceList.add(resourceEntity);
                                break;
                            case 5:
                                accountingAvailableResourceList.add(resourceEntity);
                                break;
                            case 6:
                                reportAvailableResourceList.add(resourceEntity);
                                break;
                            case 7:
                                pldAvailableResourceList.add(resourceEntity);
                                break;
                        
                            default:
                                break;
                        }
                    }
                }

                availableResourcesJson = mapper.writeValueAsString(resourceAvailableList);

                //System.out.println("availableResourcesJson: " + availableResourcesJson);
            }

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        model.addAttribute("resultStatus", resultStatus);
        model.addAttribute("resultMessage", resultMessage);

        model.addAttribute("assignedConfigurationProfileResourceList", assignedConfigurationProfileResourceList);
        model.addAttribute("resourceAvailableList", resourceAvailableList);
        model.addAttribute("availableResourcesJson", availableResourcesJson);
        model.addAttribute("profileEntity", profileEntity);
        model.addAttribute("profileResourceEntityList", profileResourceEntityList);
        model.addAttribute("resourceList", resourceEntityList);

        model.addAttribute("undefinedAvailableResourceList", undefinedAvailableResourceList);
        model.addAttribute("systemAvailableResourceList", systemAvailableResourceList);
        model.addAttribute("requestAvailableResourceList", requestAvailableResourceList);
        model.addAttribute("adminAvailableResourceList", adminAvailableResourceList);
        model.addAttribute("operationAvailableResourceList", operationAvailableResourceList);
        model.addAttribute("accountingAvailableResourceList", accountingAvailableResourceList);
        model.addAttribute("reportAvailableResourceList", reportAvailableResourceList);
        model.addAttribute("pldAvailableResourceList", pldAvailableResourceList);

        return "system/profile/update";
    }

}
