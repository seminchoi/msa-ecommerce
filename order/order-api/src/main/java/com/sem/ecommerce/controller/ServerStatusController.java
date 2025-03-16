package com.sem.ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerStatusController {
    @GetMapping("/info")
    public String checkStatus() {
        return "OK";
    }

    @GetMapping("/status")
    public String checkHealth() {
        return "OK";
    }
}
