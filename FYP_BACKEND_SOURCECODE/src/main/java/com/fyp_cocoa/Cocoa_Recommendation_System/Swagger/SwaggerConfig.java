package com.fyp_cocoa.Cocoa_Recommendation_System.Swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig { //Swagger access

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Cocoa Recommendation API")
                        .description("API for fetching Cocoa product recommendations")
                        .version("1.0")
                        .contact(new Contact().name("Bram Zoe")
                                .email("bramzoe23@gmail.com").url("re-cocoa.com"))
                        .license(new License().name("License of API")
                                .url("API license URL")));
    }
}