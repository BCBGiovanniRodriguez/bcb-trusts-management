package com.bcb.trust.front.modules.catalog.controller.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bcb.trust.front.modules.catalog.model.entity.CatalogDocumentTypeEntity;
import com.bcb.trust.front.modules.catalog.model.repository.CatalogDocumentTypeEntityRepository;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/catalog")
public class DocumentationTypeController {

    @Autowired
    private CatalogDocumentTypeEntityRepository documentTypeEntityRepository;

    @GetMapping("/document-type")
    public String index(@RequestParam(required = false) String param, Model model) {
        List<CatalogDocumentTypeEntity> documentTypeList = null;

        try {
            documentTypeList = documentTypeEntityRepository.findAll();
        } catch (Exception e) {
            System.out.println("" + e.getLocalizedMessage());
        }

        model.addAttribute("documentTypeList", documentTypeList);

        return "catalog/document-type/index";
    }

    @GetMapping("/document-type/create")
    public String create(@RequestParam(required = false) String param) {
        
        return "catalog/document-type/create";
    }
    
    
}
