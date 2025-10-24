package com.bcb.trust.front.modules.report.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/report")
public class ReportStatementMassiveController {

    @GetMapping("/statement-massive")
    public String index(@RequestParam(required = false) String param) {

        return "report/statement-massive/index";
    }


    @GetMapping("/statement-massive/create")
    public String create(@RequestParam(required = false) String param) {
        
        return "report/statement-massive/create";
    }
    

}
