package com.fyp_cocoa.Cocoa_Recommendation_System.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig { //for HTTP request for API

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}