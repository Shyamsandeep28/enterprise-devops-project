package com.company.catalogservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogController {

    @GetMapping("/products")
    public String products() {
        return "Product List";
    }

    @GetMapping("/products/{id}")
    public String productById(@PathVariable String id) {
        return "Product ID: " + id;
    }

    @GetMapping("/health")
    public String health() {
        return "Catalog Service is UP";
    }
}