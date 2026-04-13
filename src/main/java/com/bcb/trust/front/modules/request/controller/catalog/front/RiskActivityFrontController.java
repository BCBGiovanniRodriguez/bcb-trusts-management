package com.bcb.trust.front.modules.request.controller.catalog.front;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bcb.trust.front.modules.request.model.entity.catalog.RiskActivityEntity;
import com.bcb.trust.front.modules.request.model.repository.catalog.RiskActivityRepository;


@Controller
@RequestMapping("/request/catalog")
public class RiskActivityFrontController {

    @Autowired
    private RiskActivityRepository riskActivityRepository;

    @GetMapping("/risk-activity-front")
    public String index(Model model) {

        List<RiskActivityEntity> riskActivities = new ArrayList<>();

        try {
            riskActivities = riskActivityRepository.findAll();

            model.addAttribute("riskActivities", riskActivities);
        } catch (Exception e) {
            System.out.println("Error fetching risk activities: " + e.getMessage());
        }

        return "request/catalog/risk-activity/index";
    }
    

}
