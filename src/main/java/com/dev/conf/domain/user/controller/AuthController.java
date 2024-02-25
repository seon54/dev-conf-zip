package com.dev.conf.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {


    @GetMapping("/")
    public String index() {
        return "success";
    }


}
