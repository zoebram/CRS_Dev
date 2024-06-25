package com.fyp_cocoa.Cocoa_Recommendation_System.service;

import com.fyp_cocoa.Cocoa_Recommendation_System.model.CocoaProductDetail;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecommendationService { //the main recommendation algorithm
    @Autowired
    private Neo4jService neo4jService;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class); //for logging and debugging


    public List<CocoaProductDetail> getRecommendedProducts(String minRating, String minCocoaPercent, boolean includeIngredients, boolean includeFlavors, String ingredientKeywords, String flavorKeywords, boolean ingredientLogic, boolean flavorLogic, boolean combinedLogic) {
        String query = "MATCH (p:Product) WHERE p.rating >= $minRating AND p.cocoaPercent >= $minCocoaPercent ";

        if (includeIngredients) {
            query += "OPTIONAL MATCH (p)-[:CONTAINS]->(i:Ingredient) ";
        }
        if (includeFlavors) {
            query += "OPTIONAL MATCH (p)-[:HAS_FLAVOR]->(f:Flavor) ";
        }

        query += "OPTIONAL MATCH (p)-[:ORIGINATES_FROM]->(o:Origin) ";
        query += "OPTIONAL MATCH (p)-[:PRODUCED_BY]->(c:Company) ";

        query += "WITH p, o, c";
        if (includeFlavors) {
            query += ", collect(DISTINCT f.name) AS flavors";
        }
        if (includeIngredients) {
            query += ", collect(DISTINCT i.name) AS ingredients";
        }

        List<String> ingredientConditions = new ArrayList<>();
        List<String> flavorConditions = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("minRating", minRating);
        params.put("minCocoaPercent", minCocoaPercent);

        // Handling ingredient conditions
        if (includeIngredients && !ingredientKeywords.isEmpty()) {
            String[] keywords = ingredientKeywords.split(",");
            for (int idx = 0; idx < keywords.length; idx++) {
                String keyword = keywords[idx].trim();
                ingredientConditions.add("ANY(ingredient IN ingredients WHERE toLower(ingredient) CONTAINS toLower($ingredientKeyword" + idx + "))");
                params.put("ingredientKeyword" + idx, keyword);
            }
        }

        // Handling flavor conditions
        if (includeFlavors && !flavorKeywords.isEmpty()) {
            String[] keywords = flavorKeywords.split(",");
            for (int idx = 0; idx < keywords.length; idx++) {
                String keyword = keywords[idx].trim();
                flavorConditions.add("ANY(flavor IN flavors WHERE toLower(flavor) CONTAINS toLower($flavorKeyword" + idx + "))");
                params.put("flavorKeyword" + idx, keyword);
            }
        }

        // Combining conditions
        String ingredientJoiner = ingredientLogic ? " AND " : " OR ";
        String flavorJoiner = flavorLogic ? " AND " : " OR ";
        String combinedJoiner = combinedLogic ? " AND " : " OR ";

        List<String> combinedConditions = new ArrayList<>();

        if (!ingredientConditions.isEmpty()) {
            combinedConditions.add("(" + String.join(ingredientJoiner, ingredientConditions) + ")");
        }
        if (!flavorConditions.isEmpty()) {
            combinedConditions.add("(" + String.join(flavorJoiner, flavorConditions) + ")");
        }

        if (!combinedConditions.isEmpty()) {
            query += " WHERE " + String.join(combinedJoiner, combinedConditions);
        }

        // Completing the query
        query += " RETURN p.name, p.id, p.cocoaPercent, p.rating, p.numberOfIngredients, o.name AS origin, c.name AS company";
        if (includeFlavors) {
            query += ", flavors";
        }
        if (includeIngredients) {
            query += ", ingredients";
        }
        query += " ORDER BY p.rating DESC, p.cocoaPercent DESC";

        //logger.info("Executing query: {}", query);
        //logger.info("With parameters: {}", params.toString());
        List<CocoaProductDetail> results = neo4jService.executeProductDetailQuery(query, params);
        //logger.info("Number of results returned: {}", results.size());
        return results;
    }

}