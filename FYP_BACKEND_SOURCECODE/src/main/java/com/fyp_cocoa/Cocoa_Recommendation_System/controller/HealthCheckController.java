package com.fyp_cocoa.Cocoa_Recommendation_System.controller;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthCheckController { //check Status of Neo4j API

    private final Driver driver;
    public HealthCheckController(Driver driver) {
        this.driver = driver;
    }

    @GetMapping
    public ResponseEntity<String> checkNeo4jStatus() {
        try (Session session = driver.session()) {
            Result result = session.run("RETURN 1");
            if (result.hasNext()) {
                return ResponseEntity.ok("Neo4j API is running");
            } else {
                return ResponseEntity.status(500).body("Neo4j API is not accessible");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error connecting to Neo4j API: " + e.getMessage());
        }
    }
}
