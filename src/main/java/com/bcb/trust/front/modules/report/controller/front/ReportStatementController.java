package com.bcb.trust.front.modules.report.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/report")
public class ReportStatementController {

    @GetMapping("/statement")
    public String index() {
        return "report/statement/index";
    }
    
}
