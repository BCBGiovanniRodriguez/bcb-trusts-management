package com.bcb.trust.front.modules.trust.controller.front;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bcb.trust.front.modules.common.model.CommonEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustTypeEntity;
import com.bcb.trust.front.modules.trust.model.repository.TrustTrustTypeRepository;


@Controller
@RequestMapping("/catalog")
public class TrustTypeController {

    @Autowired
    private TrustTrustTypeRepository trustTypeRepository;

    @GetMapping("/trust-type")
    public String index(Model model) {

        List<TrustTrustTypeEntity> trustTypeList = new ArrayList<>();

        try {
            trustTypeList = trustTypeRepository.findByStatus(CommonEntity.STATUS_ENABLED);

        } catch (Exception e) {
            System.out.println("Error on TrustTypeController::index " + e.getLocalizedMessage());
        }

        model.addAttribute("trustTypeList", trustTypeList);

        return "catalog/trust-type/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("trustType", new TrustTrustTypeEntity());

        return "catalog/trust-type/create";
    }

    @GetMapping("/trust-type/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        TrustTrustTypeEntity trustTypeEntity = null;
        try {
            trustTypeEntity = trustTypeRepository.findById(id).get();

        } catch (Exception e) {
            // TODO: handle exception
        }

        model.addAttribute("trustTypeEntity", trustTypeEntity);

        return "catalog/trust-type/detail";
    }
    
    
}
