package com.bcb.trust.front.modules.system.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SystemUserProfileController {

    @GetMapping("/profile")
    public String getProfile() {

        return "user/index";
    }

}
