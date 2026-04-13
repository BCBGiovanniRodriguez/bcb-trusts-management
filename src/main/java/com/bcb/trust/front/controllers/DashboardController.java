package com.bcb.trust.front.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.bcb.trust.front.model.bmtkfweb.service.PartialBalanceService;
import com.bcb.trust.front.modules.system.model.entity.CatalogProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.CatalogUserEntity;
import com.bcb.trust.front.modules.system.model.repository.UserEntityRepository;
import com.bcb.trust.front.service.LegacyService;
import com.bcb.trust.front.service.MassiveReportService;
import com.bcb.trust.front.service.ReportService;

@Controller
public class DashboardController {

    @Autowired
    private UserEntityRepository systemUserEntityRepository;

    @Autowired
    ReportService reportService;

    @Autowired
    LegacyService legacyService;

    @Autowired
    MassiveReportService massiveReportService;

    @Autowired
    PartialBalanceService partialBalanceService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("user", "Usuario Fiduciario");
        model.addAttribute("workersForProcess", "Usuario Fiduciario");

        try {
            //reportService.generateReport(); // Version antigua
            //massiveReportService.process(1045); // Version nueva
            //partialBalanceService.calculatePartialBalance(1045);
        } catch (Exception e) {
            System.out.println("DashboardControllerMessage: " + e.getMessage());
        }

        return "dashboard/index";
    }

    @ModelAttribute("systemUserEntity")
    public CatalogUserEntity systemUserEntity(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        CatalogUserEntity systemUserEntity = systemUserEntityRepository.findByNickname(userDetails.getUsername());
        return systemUserEntity;
    }

    /*
     * @ModelAttribute("systemProfileEntity")
     * public SystemProfileEntity systemProfileEntity(Authentication authentication)
     * {
     * UserDetails userDetails = (UserDetails) authentication.getPrincipal();
     * SystemUserEntity systemUserEntity =
     * systemUserEntityRepository.findByNickname(userDetails.getUsername());
     * return systemUserEntity.getProfile();
     * }
     */
}
