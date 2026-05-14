package com.company.loginservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "Login Successful";
    }

    @GetMapping("/register")
    public String register() {
        return "User Registered";
    }

    @GetMapping("/health")
    public String health() {
        return "Login Service is UP";
    }
}