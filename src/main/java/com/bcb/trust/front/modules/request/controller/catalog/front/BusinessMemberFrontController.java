package com.bcb.trust.front.modules.request.controller.catalog.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bcb.trust.front.modules.request.model.entity.catalog.BusinessMemberEntity;
import com.bcb.trust.front.modules.request.model.repository.catalog.BusinessMemberRepository;


@Controller
@RequestMapping("/request/catalog")
public class BusinessMemberFrontController {

    @Autowired
    private BusinessMemberRepository businessMemberRepository;

    @GetMapping("/business-member-front")
    public String getMethodName(Model model) {

        List<BusinessMemberEntity> businessMembers = businessMemberRepository.findAll();
        model.addAttribute("businessMembers", businessMembers);

        return "request/catalog/business-member/index";
    }

}
