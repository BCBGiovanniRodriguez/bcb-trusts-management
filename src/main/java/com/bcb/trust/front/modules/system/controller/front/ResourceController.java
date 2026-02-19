package com.bcb.trust.front.modules.system.controller.front;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bcb.trust.front.modules.system.model.entity.CatalogResourceEntity;
import com.bcb.trust.front.modules.system.model.repository.ResourceRepository;

@Controller
@RequestMapping("/system")
public class ResourceController {

    @Autowired
    private ResourceRepository systemResourceRepository;

    @GetMapping("/resource")
    public String list(@RequestParam(required = false) Integer module, Model model) {
        List<CatalogResourceEntity> resourceList = new ArrayList<>();
        String[] moduleNames = null;

        try {
            moduleNames = CatalogResourceEntity.moduleNames;

            if (module != null) {
                resourceList = systemResourceRepository.findByModule(module);
            } else {
                module = 0;
                // resourceList = systemResourceRepository.findAll();
            }

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        model.addAttribute("moduleQueryParam", (Integer) module);
        model.addAttribute("moduleAsString", moduleNames[module]);
        model.addAttribute("moduleNames", moduleNames);
        model.addAttribute("resourceList", resourceList);

        return "system/resource/index";
    }

}
