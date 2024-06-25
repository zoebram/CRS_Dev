// src/main/java/com/fyp_cocoa/Cocoa_Recommendation_System/config/Neo4jConfig.java
package com.fyp_cocoa.Cocoa_Recommendation_System.config;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fyp_cocoa.Cocoa_Recommendation_System.service.RecommendationService;
@Configuration
public class Neo4jConfig { //neo4j access

    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class); //for logging and debugging
    @Value("${server.port}") //taken from application.yml
    private String serverPort;

    @Value("${spring.neo4j.authentication.username}") //taken from application.yml
    private String username;

    @Value("${spring.neo4j.authentication.password}") //taken from application.yml
    private String password; 

    @Value("${spring.neo4j.uri}")//taken from application.yml
    private String uri;

    @Bean
    public Driver neo4jDriver() {
        //logger.info("Connecting to Neo4j with URI: {}", uri);
        //logger.info("Using username: {}", username);
        if (uri == null || username == null || password == null) {
            throw new IllegalArgumentException("Neo4j connection parameters not set in environment variables.");
        }
        return GraphDatabase.driver(uri, AuthTokens.basic(username, password));
    }


}
