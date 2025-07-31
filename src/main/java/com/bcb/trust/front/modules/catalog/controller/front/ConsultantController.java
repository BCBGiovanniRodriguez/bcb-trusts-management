package com.bcb.trust.front.modules.catalog.controller.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bcb.trust.front.modules.catalog.model.entity.CatalogAddressEntity;
import com.bcb.trust.front.modules.catalog.model.entity.CatalogPersonEntity;
import com.bcb.trust.front.modules.catalog.model.repository.CatalogConsultantRepository;
import com.bcb.trust.front.modules.system.model.entity.SystemProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemUserEntityRepository;

@Controller
@RequestMapping("/catalog/consultant")
public class ConsultantController {

    @Autowired
    private SystemUserEntityRepository systemUserEntityRepository;

    @Autowired
    private CatalogConsultantRepository consultantRepository;

    @GetMapping("/")
    public String index() {

        return "catalog/consultant/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        CatalogPersonEntity person = new CatalogPersonEntity();
        CatalogAddressEntity address = new CatalogAddressEntity();

        model.addAttribute("person", person);
        model.addAttribute("address", address);
        
        return "catalog/consultant/create";
    }

    @PostMapping("/create")
    public String createSubmit(Model model) {
        
        
        return "catalog/consultant/create";
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
