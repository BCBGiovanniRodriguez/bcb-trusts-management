package com.bcb.trust.front.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bcb.trust.front.model.bmtkfweb.service.PartialBalanceService;
import com.bcb.trust.front.service.LegacyService;
import com.bcb.trust.front.service.MassiveReportService;
import com.bcb.trust.front.service.ReportService;


@Controller
public class DashboardController {


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

        //reportService.exportPDF("Example");

        //int totalWorkers = legacyService.getTotalWorkers(1045);

        try {
            //reportService.generateReport();
            massiveReportService.process(1045);
            //partialBalanceService.calculatePartialBalance(1045);
        } catch (Exception e) {
            System.out.println("DashboardControllerMessage: " + e.getMessage());
        }

        return "dashboard/index";
    }
    
}
