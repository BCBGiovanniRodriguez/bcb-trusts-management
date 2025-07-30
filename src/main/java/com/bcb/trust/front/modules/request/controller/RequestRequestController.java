package com.bcb.trust.front.modules.request.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/request")
public class RequestRequestController {

    @PostMapping("/create")
    public String saveRequest(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
}
