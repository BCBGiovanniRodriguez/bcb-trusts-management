package com.bcb.trust.front.modules.trust.controller.front;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bcb.trust.front.modules.trust.model.entity.TrustTrustEntity;
import com.bcb.trust.front.modules.trust.model.repository.TrustTrustRepository;

@Controller
@RequestMapping("/trust")
public class TrustController {

    public static final Logger logger = Logger.getLogger(TrustController.class.getName());

    @Autowired
    private TrustTrustRepository trustRepository;

    @GetMapping("/trust")
    public String index(@RequestParam(required = false) Integer state, 
            @RequestParam(required = false) Integer status, Model model) {

        List<TrustTrustEntity> trustList = new ArrayList<>();

        try {
            if (state != null && status != null) {
                trustList = trustRepository.findByStateAndStatus(state, status);
            } else if(state != null && status == null) {
                trustList = trustRepository.findByState(state);
            } else if(state == null && status != null) {
                trustList = trustRepository.findByStatus(status);
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getLocalizedMessage());
        }

        model.addAttribute("trustList", trustList);
        model.addAttribute("stateList", TrustTrustEntity.states);
        model.addAttribute("statusList", TrustTrustEntity.statuses);

        return "trust/trust/index";
    }
    
}
