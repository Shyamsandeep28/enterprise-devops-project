package com.company.loginservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

@GetMapping("/login")
public String login() {

    return """
            <h1>Login Successful</h1>

            <a href='http://65.2.83.97:30082/products'>
                Open Product Catalog
            </a>
            """;
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
