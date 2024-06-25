package com.fyp_cocoa.Cocoa_Recommendation_System.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fyp_cocoa.Cocoa_Recommendation_System.model.CocoaProduct;
import com.fyp_cocoa.Cocoa_Recommendation_System.model.CocoaProductDetail;
import com.fyp_cocoa.Cocoa_Recommendation_System.model.Company;
import com.fyp_cocoa.Cocoa_Recommendation_System.service.CompanyService;
import com.fyp_cocoa.Cocoa_Recommendation_System.service.IngredientService;
import com.fyp_cocoa.Cocoa_Recommendation_System.service.ProductService;
import com.fyp_cocoa.Cocoa_Recommendation_System.service.RecommendationService;

@RestController
@RequestMapping("/products")
public class CocoaProductController { //for the method retrieving data of cocoa

    @Autowired
    private ProductService productService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private RecommendationService recommendationService;

    private static final Logger logger = LoggerFactory.getLogger(CocoaProductController.class); //for logging and debugging


    @Cacheable(value = "flavorCache", key = "#flavorName")
    @GetMapping("/flavor/{flavorName}")
    public ResponseEntity<?> getProductsByFlavor(@PathVariable String flavorName) {
        List<CocoaProduct> products = productService.getProductsByFlavor(flavorName);
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @Cacheable(value = "productCache", key = "#name")
    @GetMapping("/details/by-name/{name}")
    public List<CocoaProductDetail> getProductsByName(@PathVariable String name) {
        //logger.info("Fetching products by product: {}", name);
        return productService.getProductsByName(name);
    }

    @Cacheable(value = "productCache", key = "#id")
    @GetMapping("/details/by-id/{id}")
    public List<CocoaProductDetail> getProductsDetailsById(@PathVariable String id) {
        //logger.info("Fetching products by product: {}", id);
        return productService.getProductsDetailsById(id);
    }

    @Cacheable(value = "flavorCache", key = "#flavorName")
    @GetMapping("/detailsByFlavor/{flavorName}")
    public List<CocoaProductDetail> getDetailedProductsByFlavor(@PathVariable String flavorName) {
        //logger.info("Fetching products by flavor: {}", flavorName);
        return productService.getDetailedProductsByFlavor(flavorName);
    }
    @Cacheable(value = "companyCache", key = "#companyName")
    @GetMapping("/company/{companyName}")
    public List<CocoaProduct> getProductsByCompany(@PathVariable String companyName) {
        //logger.info("Fetching products by company: {}", companyName);
        return companyService.getProductsByCompany(companyName);
    }
    @Cacheable(value = "companyCache", key = "#companyName")

    @GetMapping("/detailsByCompany/{companyName}")
    public List<CocoaProductDetail> getDetailedProductsByCompany(@PathVariable String companyName) {
        //logger.info("Fetching products by company: {}", companyName);
        return companyService.getDetailedProductsByCompany(companyName);
    }
    @GetMapping("/companyName/{companyName}")
    public List<Company> getCompanyName(@PathVariable String companyName) {
        //logger.info("Fetching products by company: {}", companyName);
        return companyService.getCompanyName(companyName);
    }
    @Cacheable(value = "ingredientCache", key = "#ingredientName")

    @GetMapping("/ingredient/{ingredientName}")
    public List<CocoaProduct> getProductsByIngredient(@PathVariable String ingredientName) {
        //logger.info("Fetching products by ingredients: {}", ingredientName);
        return ingredientService.getProductsByIngredient(ingredientName);
    }

    @Cacheable(value = "ingredientCache", key = "#ingredientName")

    @GetMapping("/details/ingredient/{ingredientName}")
    public List<CocoaProductDetail> getDetailedProductsByIngredient(@PathVariable String ingredientName) {
        //logger.info("Fetching products by ingredients: {}", ingredientName);
        return ingredientService.getDetailedProductsByIngredient(ingredientName);
    }

    @GetMapping("/recommended")
    public ResponseEntity<?> getRecommendedProducts(
            @RequestParam(required = false, defaultValue = "1") String minRating,
            @RequestParam(required = false, defaultValue = "50") String minCocoaPercent,
            @RequestParam(required = false, defaultValue = "false") boolean includeIngredients,
            @RequestParam(required = false, defaultValue = "false") boolean includeFlavors,
            @RequestParam(required = false, defaultValue = "") String ingredientKeywords,
            @RequestParam(required = false, defaultValue = "") String flavorKeywords,
            @RequestParam(required = false, defaultValue = "false") boolean ingredientLogic,
            @RequestParam(required = false, defaultValue = "false") boolean flavorLogic,
            @RequestParam(required = false, defaultValue = "false") boolean combinedLogic
    ) {
        try {
            List<CocoaProductDetail> products = recommendationService.getRecommendedProducts(
                    minRating, minCocoaPercent, includeIngredients, includeFlavors, ingredientKeywords, flavorKeywords, ingredientLogic, flavorLogic, combinedLogic
            );
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            // To log the exception or handle it
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request: " + e.getMessage());
        }
    }
}
