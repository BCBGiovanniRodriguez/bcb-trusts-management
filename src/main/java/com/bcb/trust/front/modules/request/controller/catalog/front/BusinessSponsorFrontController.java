package com.bcb.trust.front.modules.request.controller.catalog.front;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bcb.trust.front.modules.system.model.entity.CatalogProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.CatalogUserEntity;
import com.bcb.trust.front.modules.system.model.entity.ConfigurationUserProfileEntity;
import com.bcb.trust.front.modules.system.model.repository.ConfigurationUserProfileRepository;
import com.bcb.trust.front.modules.system.model.repository.ProfileRepository;

@Controller
@RequestMapping("/request/catalog")
public class BusinessSponsorFrontController {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ConfigurationUserProfileRepository configurationUserProfileRepository;

    private static final String SPONSOR_CODE = "STOCK_EXCHANGE_PROMOTION_SPONSOR";

    @GetMapping("/business-sponsor-front")
    public String index(Model model) {
        List<CatalogUserEntity> businessSponsorList = new ArrayList<>();

        try {
            Optional<CatalogProfileEntity> sponsorProfile = profileRepository.findOneByCode(SPONSOR_CODE);
            List<ConfigurationUserProfileEntity> configurationUserProfiles = configurationUserProfileRepository.findByProfileEntity(sponsorProfile.get());
    
            for (ConfigurationUserProfileEntity configurationUserProfileEntity : configurationUserProfiles) {
                if (configurationUserProfileEntity.getActive() == 1) {
                    businessSponsorList.add(configurationUserProfileEntity.getUserEntity());
                }
            }

            System.out.println("Fetched " + businessSponsorList.size() + " business sponsors for profile code: " + SPONSOR_CODE);
        } catch (Exception e) {
            // TODO: handle exception
        }

        model.addAttribute("businessConsultantList", businessSponsorList);

        return "request/catalog/business-sponsor/index";
    }
    
}
