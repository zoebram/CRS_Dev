package com.fyp_cocoa.Cocoa_Recommendation_System.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {

    @Value("${spring.neo4j.uri}")//taken from application.yml
    private String neo4jUrl; //change accordingly to your neo4j uri

    @GetMapping("/api/config")
    public Map<String, String> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("neo4jUrl", neo4jUrl);
        return config;
    }
}
