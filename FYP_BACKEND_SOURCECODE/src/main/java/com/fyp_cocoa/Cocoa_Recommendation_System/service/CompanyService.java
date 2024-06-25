package com.fyp_cocoa.Cocoa_Recommendation_System.service;

import com.fyp_cocoa.Cocoa_Recommendation_System.model.CocoaProduct;
import com.fyp_cocoa.Cocoa_Recommendation_System.model.CocoaProductDetail;
import com.fyp_cocoa.Cocoa_Recommendation_System.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CompanyService { //Query definition for fetching info related to Company node

    @Autowired
    private Neo4jService neo4jService;


    public List<CocoaProduct> getProductsByCompany(String companyName) {
        String query = "MATCH (p:Product)-[:PRODUCED_BY]->(c:Company) WHERE toLower(c.name) = toLower($companyName) RETURN p.name, p.id, p.cocoaPercent, p.rating, p.numberOfIngredients";
        Map<String, Object> params = Map.of("companyName", companyName);
        return neo4jService.executeProductQuery(query, params);
    }

    public List<CocoaProductDetail> getDetailedProductsByCompany(String companyName) {
        String query = "MATCH (p:Product)-[:PRODUCED_BY]->(c:Company) WHERE c.name = $companyName " +
                "OPTIONAL MATCH (p)-[:ORIGINATES_FROM]->(o:Origin) " +
                "OPTIONAL MATCH (p)-[:PRODUCED_BY]->(c:Company) " +
                "OPTIONAL MATCH (p)-[:HAS_FLAVOR]->(f:Flavor) " +
                "OPTIONAL MATCH (p)-[:CONTAINS]->(i:Ingredient) " +
                "RETURN p.name, p.id, p.cocoaPercent, p.rating, p.numberOfIngredients , o.name AS origin, c.name AS company, " +
                "collect(DISTINCT f.name) AS flavors, collect(DISTINCT i.name) AS ingredients";
        Map<String, Object> params = Map.of("companyName", companyName);
        return neo4jService.executeProductDetailQuery(query, params);
    }

    public List<Company> getCompanyName(String companyName) {
        String query = "MATCH (c:Company) WHERE toLower(c.name) CONTAINS toLower($companyName) " +
                "OPTIONAL MATCH (c)-[:LOCATED_IN]->(l:Location) " +
                "OPTIONAL MATCH (p:Product)-[:PRODUCED_BY]->(c:Company) " +
                "RETURN c.name AS company, l.name AS location, p.name AS product";
        Map<String, Object> params = Map.of("companyName", companyName);
        return neo4jService.executeCompanyQuery(query, params);
    }
}
