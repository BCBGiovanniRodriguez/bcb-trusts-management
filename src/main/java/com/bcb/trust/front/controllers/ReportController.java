package com.bcb.trust.front.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bcb.trust.front.service.UnprocessedWorkersService;

@Controller
public class ReportController {

    @Autowired
    UnprocessedWorkersService unprocessedWorkedService;

    @GetMapping("/process")
    public String report(Model model) {
        //model.addAttribute("user", model);

        try {
            unprocessedWorkedService.process(1045);
        } catch (Exception e) {
            System.out.println("ReportController::report " + e.getLocalizedMessage());
        }

        return "report/process";
    }
}
