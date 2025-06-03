package com.bcb.trust.front.controllers.request;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@Controller
@RequestMapping("/request")
public class RequestController {

    @GetMapping("/request")
    public String queryForm(@RequestParam(required = false) String param) {
        return "request/request/index";
    }

    @PostMapping("/request")
    public String querySubmit(@RequestBody(required = false) String entity) {
        //TODO: process POST request
        
        return "request/request/index";
    }
    

    @GetMapping("/request/create")
    public String requestCreateForm(@RequestParam(required = false) String param) {
        return "request/request/create";
    }

    @PostMapping("/request/create")
    public String requestCreateSubmit(@RequestBody(required = false) String entity) {
        
        return "request/request/create";
    }
    
}
