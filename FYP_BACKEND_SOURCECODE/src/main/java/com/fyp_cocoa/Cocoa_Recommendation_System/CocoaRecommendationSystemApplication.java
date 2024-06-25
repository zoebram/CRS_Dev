package com.fyp_cocoa.Cocoa_Recommendation_System;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class CocoaRecommendationSystemApplication { //main app

	public static void main(String[] args) {
		SpringApplication.run(CocoaRecommendationSystemApplication.class, args);
	}

	@Controller
	public static class Routes{

		@GetMapping("/{path:[^\\.]*}")
		public String redirect(){
			return "forward:/";
		}
	}
}
