package com.bcb.trust.front.process;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;

import com.bcb.trust.front.service.ReportService;

public class ReportProcessImpl  implements Process {

    @Autowired
    ReportService reportService;

    @Override
    public void process() {
        try {
            reportService.generateReport();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        
    }


}
