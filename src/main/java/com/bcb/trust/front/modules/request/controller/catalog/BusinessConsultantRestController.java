package com.bcb.trust.front.modules.request.controller.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcb.trust.front.modules.system.model.entity.CatalogProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.CatalogUserEntity;
import com.bcb.trust.front.modules.system.model.entity.ConfigurationUserProfileEntity;
import com.bcb.trust.front.modules.system.model.repository.ConfigurationUserProfileRepository;
import com.bcb.trust.front.modules.system.model.repository.ProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/request/catalog")
public class BusinessConsultantRestController {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ConfigurationUserProfileRepository configurationUserProfileRepository;

    private static final String CONSULTANT_CODE = "STOCK_EXCHANGE_PROMOTION_CONSULTANT";

    @GetMapping("/business-consultant")
    public String getAll() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = null;
        List<Map<String, Object>> resultList = new ArrayList<>();

        List<CatalogUserEntity> users = new ArrayList<>();

        try {

            Optional<CatalogProfileEntity> consultantProfile = profileRepository.findOneByCode(CONSULTANT_CODE);
            List<ConfigurationUserProfileEntity> configurationUserProfiles = configurationUserProfileRepository.findByProfileEntity(consultantProfile.get());

            for (ConfigurationUserProfileEntity configurationUserProfileEntity : configurationUserProfiles) {
                if (configurationUserProfileEntity.getActive() == 1) {
                    users.add(configurationUserProfileEntity.getUserEntity());
                    resultList.add(configurationUserProfileEntity.getUserEntity().toMap());
                }
            }

            jsonResponse = mapper.writeValueAsString(resultList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }
    
}
