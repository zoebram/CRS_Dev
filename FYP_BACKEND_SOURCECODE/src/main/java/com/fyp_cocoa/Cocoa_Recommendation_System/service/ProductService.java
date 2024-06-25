package com.fyp_cocoa.Cocoa_Recommendation_System.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fyp_cocoa.Cocoa_Recommendation_System.model.CocoaProduct;
import com.fyp_cocoa.Cocoa_Recommendation_System.model.CocoaProductDetail;

@Service
public class ProductService { //Query definition for fetching info related to Product node AND Flavor node

    @Autowired
    private Neo4jService neo4jService;

    public List<CocoaProduct> getProductsByFlavor(String flavorName) {
        String query = "MATCH (p:Product)-[:HAS_FLAVOR]->(f:Flavor) WHERE toLower(f.name) CONTAINS toLower($flavorName) RETURN p.name, p.id, p.cocoaPercent, p.rating, p.numberOfIngredients";
        Map<String, Object> params = Map.of("flavorName", flavorName);
        return neo4jService.executeProductQuery(query, params);
    }

    public List<CocoaProductDetail> getProductsByName(String name) {
        String query = "MATCH (p:Product) " +
                "WHERE toLower(p.name) CONTAINS toLower($name) " +
                "OPTIONAL MATCH (p)-[:ORIGINATES_FROM]->(o:Origin) " +
                "OPTIONAL MATCH (p)-[:PRODUCED_BY]->(c:Company) " +
                "OPTIONAL MATCH (p)-[:HAS_FLAVOR]->(f:Flavor) " +
                "OPTIONAL MATCH (p)-[:CONTAINS]->(i:Ingredient) " +
                "RETURN p.name, p.id, p.cocoaPercent, p.rating, p.numberOfIngredients , o.name AS origin, c.name AS company, " +
                "collect(DISTINCT f.name) AS flavors, collect(DISTINCT i.name) AS ingredients";
        Map<String, Object> params = Map.of("name", name);
        return neo4jService.executeProductDetailQuery(query, params);
    }

    public List<CocoaProductDetail> getProductsDetailsById(String id) {
        String query = "MATCH (p:Product) " + // Match by id
                "WHERE p.id = $id " +
                "OPTIONAL MATCH (p)-[:ORIGINATES_FROM]->(o:Origin) " +
                "OPTIONAL MATCH (p)-[:PRODUCED_BY]->(c:Company) " +
                "OPTIONAL MATCH (p)-[:HAS_FLAVOR]->(f:Flavor) " +
                "OPTIONAL MATCH (p)-[:CONTAINS]->(i:Ingredient) " +
                "RETURN p.name, p.id, p.cocoaPercent, p.rating, p.numberOfIngredients, o.name AS origin, c.name AS company, " +
                "collect(DISTINCT f.name) AS flavors, collect(DISTINCT i.name) AS ingredients";
        Map<String, Object> params = Map.of("id", id);
        return neo4jService.executeProductDetailQuery(query, params);
    }

    public List<CocoaProductDetail> getDetailedProductsByFlavor(String flavorName) {
        String query = "MATCH (p:Product)-[:HAS_FLAVOR]->(f:Flavor) WHERE f.name = $flavorName " +
                "OPTIONAL MATCH (p)-[:ORIGINATES_FROM]->(o:Origin) " +
                "OPTIONAL MATCH (p)-[:PRODUCED_BY]->(c:Company) " +
                "OPTIONAL MATCH (p)-[:HAS_FLAVOR]->(tf:Flavor) " +
                "OPTIONAL MATCH (p)-[:CONTAINS]->(i:Ingredient) " +
                "RETURN p.name, p.id, p.cocoaPercent, p.rating, p.numberOfIngredients , o.name AS origin, c.name AS company, " +
                "collect(DISTINCT tf.name) AS flavors, collect(DISTINCT i.name) AS ingredients";
        Map<String, Object> params = Map.of("flavorName", flavorName);
        return neo4jService.executeProductDetailQuery(query, params);
    }
}
