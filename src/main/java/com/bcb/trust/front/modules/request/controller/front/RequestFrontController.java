package com.bcb.trust.front.modules.request.controller.front;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bcb.trust.front.modules.common.model.CommonEntity;
import com.bcb.trust.front.modules.request.model.entity.RequestRequestEntity;
import com.bcb.trust.front.modules.request.model.entity.catalog.BusinessTypeEntity;
import com.bcb.trust.front.modules.request.model.repository.RequestEntityRepository;
import com.bcb.trust.front.modules.request.model.repository.catalog.BusinessTypeRepository;
import com.bcb.trust.front.modules.trust.model.entity.TrustCatalogTrustTypeEntity;
import com.bcb.trust.front.modules.trust.model.repository.TrustTrustTypeRepository;

@Controller
@RequestMapping("/request")
public class RequestFrontController {

    @Autowired
    private TrustTrustTypeRepository trustTypeRepository;

    @Autowired
    private BusinessTypeRepository businessTypeRepository;

    @Autowired
    private RequestEntityRepository requestEntityRepository;

    @GetMapping("/request")
    public String index(@RequestParam(required = false) String param, Model model) {
        List<RequestRequestEntity> requestList = new ArrayList<>();
        List<BusinessTypeEntity> businessTypeList = new ArrayList<>();

        try {
            businessTypeList = businessTypeRepository.findByStatus(CommonEntity.STATUS_ENABLED);
            requestList = requestEntityRepository.findAll();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        model.addAttribute("requestList", requestList);
        model.addAttribute("businessTypeList", businessTypeList);

        return "request/request/index";
    }

    @GetMapping("/request/detail/{id}")
    public String detail(@PathVariable Long id) {

        return "request/request/detail";
    }

    @GetMapping("/request/create")
    public String requestCreateForm(@RequestParam(required = false) String param, Model model) {
        List<TrustCatalogTrustTypeEntity> trustTypeEntityList = new ArrayList<>();

        try {
            trustTypeEntityList = trustTypeRepository.findByStatus(CommonEntity.STATUS_ENABLED);

        } catch (Exception e) {
            System.out.println("Error: " + e.getLocalizedMessage());
        }

        model.addAttribute("trustTypeEntityList", trustTypeEntityList);

        return "request/request/create";
    }

    @PostMapping("/request/create")
    public String requestCreateSubmit(@RequestBody(required = false) String entity) {

        return "request/request/create";
    }

}
