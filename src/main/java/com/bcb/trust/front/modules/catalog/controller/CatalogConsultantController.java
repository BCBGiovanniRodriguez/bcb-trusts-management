package com.bcb.trust.front.modules.catalog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcb.trust.front.model.ResultResponse;
import com.bcb.trust.front.modules.catalog.model.entity.CatalogConsultant;
import com.bcb.trust.front.modules.catalog.model.repository.CatalogConsultantRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/catalog")
public class CatalogConsultantController {

    @Autowired
    private CatalogConsultantRepository catalogConsultantRepository;

    @PostMapping("/consultant")
    public ResultResponse<CatalogConsultant> create(@RequestBody CatalogConsultant consultant) {
        ResultResponse<CatalogConsultant> result = null;

        try {
            catalogConsultantRepository.saveAndFlush(consultant);
            result = ResultResponse.success(consultant);
        } catch (Exception e) {
            result = ResultResponse.failure("Error on: CatalogConsultantController::create[" + e.getLocalizedMessage()+"]");
        }
        
        return result;
    }

    @PutMapping("/consultant/{id}")
    public ResultResponse<CatalogConsultant> putMethodName(@PathVariable String id, @RequestBody CatalogConsultant consultant) {
        ResultResponse<CatalogConsultant> result = null;
        
        try {
            Optional<CatalogConsultant> optional = catalogConsultantRepository.findById(Long.parseLong(id));

            if (optional.isPresent()) {
                optional.get()
                    .setStatus(consultant.getStatus());
                
                result = ResultResponse.success(consultant);
            } else {
                result = ResultResponse.failure("Registro con identificador '" + id + "'' no encontrado");
            }

        } catch (Exception e) {
            result = ResultResponse.failure("Error on: CatalogConsultantController::create[" + e.getLocalizedMessage()+"]");
        }
        
        return result;
    }
    
}
