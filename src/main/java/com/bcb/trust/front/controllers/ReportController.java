package com.bcb.trust.front.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialPeriodEntity;
import com.bcb.trust.front.modules.trust.model.repository.TrustSpecialPeriodRepository;
import com.bcb.trust.front.modules.trust.service.BalanceWorkerService;
import com.bcb.trust.front.modules.trust.service.MigrationWorkerService;
import com.bcb.trust.front.service.ReportService;
import com.bcb.trust.front.service.UnprocessedWorkersService;


@Controller
public class ReportController {

    @Autowired
    UnprocessedWorkersService unprocessedWorkedService;

    @Autowired
    MigrationWorkerService migrationWorkerService;

    @Autowired
    BalanceWorkerService balanceWorkerService;

    @Autowired
    TrustSpecialPeriodRepository periodRepository;

    @Autowired
    ReportService reportService;

    @GetMapping("/process")
    public String report(Model model) {
        try {
            //unprocessedWorkedService.process(1045);
            TrustSpecialPeriodEntity startReportPeriod = periodRepository.findById(38L).orElse(null);
            TrustSpecialPeriodEntity endReportPeriod = periodRepository.findById(43L).orElse(null);
            TrustSpecialPeriodEntity startIntermediatePeriod = periodRepository.findById(2L).orElse(null);
            TrustSpecialPeriodEntity endIntermediatePeriod = periodRepository.findById(37L).orElse(null);

            reportService.setStartReportPeriod(startReportPeriod);
            reportService.setEndReportPeriod(endReportPeriod);

            reportService.setStartIntermediatePeriod(startIntermediatePeriod);
            reportService.setEndIntermediatePeriod(endIntermediatePeriod);
            
            reportService.generateReport2(1045);
        } catch (Exception e) {
            System.out.println("ReportController::report " + e.getLocalizedMessage());
        }

        return "report/process";
    }

    @GetMapping("/migrate-workers")
    public String migrateWorkers() {

        migrationWorkerService.checkWorkers(1045);
        return "report/worker-migration";
    }
    
    @GetMapping("/migrate-balances")
    public String migrateBalances() {

        balanceWorkerService.generateBalance(1045);
        return "report/balance-migration";
    }
    
    @GetMapping("/migrate-annual-balances")
    public String migrateAnualBalances() {

        balanceWorkerService.generateYearBalance(1045);
        return "report/balance-migration";
    }
}
