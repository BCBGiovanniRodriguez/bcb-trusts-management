package com.bcb.trust.front.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bcb.trust.front.model.trusts.entity.ProcessEntity;
import com.bcb.trust.front.model.trusts.repository.ProcessRepository;

@Controller
public class ReportBalanceController {

    @Autowired
    ProcessRepository processRepository;

    @GetMapping("/balance")
    public String index(Model model) {

        List<ProcessEntity> processList = processRepository.findAll();

        model.addAttribute("processList", processList);

        return "balance/index";
    }

    @GetMapping("/balance/generate")
    public String generate() {

        

        return "balance/massive";
    }

    
}
