package com.fyp_cocoa.Cocoa_Recommendation_System.service;

import com.fyp_cocoa.Cocoa_Recommendation_System.model.CocoaProduct;
import com.fyp_cocoa.Cocoa_Recommendation_System.model.CocoaProductDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IngredientService { //Query definition for fetching info related to Ingredient node


    @Autowired
    private Neo4jService neo4jService;

    public List<CocoaProduct> getProductsByIngredient(String ingredientName) {
        String query = "MATCH (p:Product)-[:CONTAINS]->(i:Ingredient) WHERE toLower(i.name) CONTAINS toLower($ingredientName) RETURN p.name, p.id, p.cocoaPercent, p.rating, p.numberOfIngredients";
        Map<String, Object> params = Map.of("ingredientName", ingredientName);
        return neo4jService.executeProductQuery(query, params);
    }

    public List<CocoaProductDetail> getDetailedProductsByIngredient(String ingredientName) {
        String query = "MATCH (p:Product)-[:CONTAINS]->(i:Ingredient) WHERE toLower(i.name) CONTAINS toLower($ingredientName) " +
                "OPTIONAL MATCH (p)-[:ORIGINATES_FROM]->(o:Origin) " +
                "OPTIONAL MATCH (p)-[:PRODUCED_BY]->(c:Company) " +
                "OPTIONAL MATCH (p)-[:HAS_FLAVOR]->(f:Flavor) " +
                "OPTIONAL MATCH (p)-[:CONTAINS]->(ti:Ingredient) " +
                "RETURN p.name, p.id, p.cocoaPercent, p.rating, p.numberOfIngredients , o.name AS origin, c.name AS company, " +
                "collect(DISTINCT f.name) AS flavors, collect(DISTINCT ti.name) AS ingredients";
        Map<String, Object> params = Map.of("ingredientName", ingredientName);
        return neo4jService.executeProductDetailQuery(query, params);
    }
}
