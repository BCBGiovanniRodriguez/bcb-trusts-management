package com.bcb.trust.front.controllers.catalog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bcb.trust.front.model.trusts.entity.catalog.TrustType;
import com.bcb.trust.front.model.trusts.enums.StatusEnum;
import com.bcb.trust.front.model.trusts.repository.catalog.TrustTypeRepository;

@Controller
@RequestMapping("/catalog/trust-type")
public class TrustTypeController {

    @Autowired
    private TrustTypeRepository trustTypeRepository;

    @GetMapping("/")
    public String index(Model model) {

        List<TrustType> trustTypeList = new ArrayList<>();

        try {
            StatusEnum status = StatusEnum.ENABLED;
            trustTypeList = trustTypeRepository.findByStatus(status);

        } catch (Exception e) {
            System.out.println("Error on TrustTypeController::index " + e.getLocalizedMessage());
        }

        model.addAttribute("trustTypeList", trustTypeList);

        return "catalog/trust-type/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("trustType", new TrustType());

        return "catalog/trust-type/create";
    }

    @PostMapping("/create")
    public String createSubmit(@ModelAttribute TrustType trustType) {
        LocalDateTime now = LocalDateTime.now();

        try {
            trustType.setStatus(StatusEnum.ENABLED);
            trustType.setCreated(now);
        } catch (Exception e) {
            System.out.println("Error on TrustTypeController::create " + e.getLocalizedMessage());
        }
        
        return "catalog/trust-type/create";
    }
    
    
    
}
