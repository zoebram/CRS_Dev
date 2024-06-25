package com.fyp_cocoa.Cocoa_Recommendation_System.service;

import com.fyp_cocoa.Cocoa_Recommendation_System.model.CocoaProduct;
import com.fyp_cocoa.Cocoa_Recommendation_System.model.CocoaProductDetail;
import com.fyp_cocoa.Cocoa_Recommendation_System.model.Company;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class Neo4jService { //to initialize neo4j driver for connection between neo4j database and this app

    private final Driver driver;
    public Neo4jService(Driver driver) {
        this.driver = driver;
    }

    @PostConstruct
    public void init() {
        // Ensuring the driver is initialized
        driver.session();
    }

    @PreDestroy
    public void close() {
        // Properly closing the driver
        driver.close();
    }

    private String getStringFromRecord(Record record, String key) {
        return record.get(key).asString(null);
    }

    private double getDoubleFromRecord(Record record, String key) {
        return record.get(key).isNull() ? 0.0 : Double.parseDouble(record.get(key).asString());
    }

    private int getIntFromRecord(Record record, String key) {
        return record.get(key).isNull() ? 0 : Integer.parseInt(record.get(key).asString());
    }

    private <T> T mapRecordToModel(Record record, Function<Record, T> mapper) {
        return mapper.apply(record);
    }

    private CocoaProduct mapToCocoaProduct(Record record) {
        CocoaProduct product= new CocoaProduct();
        product.setId(getStringFromRecord(record, "p.id"));
        product.setName(getStringFromRecord(record, "p.name"));
        product.setCocoaPercent(getDoubleFromRecord(record, "p.cocoaPercent"));
        product.setRating(getDoubleFromRecord(record, "p.rating"));
        product.setNumberOfIngredients(getIntFromRecord(record, "p.numberOfIngredients"));
        return product;
    }

    private CocoaProductDetail mapToCocoaProductDetail(Record record) {
        CocoaProductDetail detail = new CocoaProductDetail();
        detail.setId(getStringFromRecord(record, "p.id"));
        detail.setName(getStringFromRecord(record, "p.name"));
        detail.setCocoaPercent(getDoubleFromRecord(record, "p.cocoaPercent"));
        detail.setRating(getDoubleFromRecord(record, "p.rating"));
        detail.setNumberOfIngredients(getIntFromRecord(record, "p.numberOfIngredients"));
        detail.setOrigin(getStringFromRecord(record, "origin"));
        detail.setCompany(getStringFromRecord(record, "company"));
        detail.setFlavors(record.containsKey("flavors") ? record.get("flavors").asList(Value::asString) : new ArrayList<>());
        detail.setIngredients(record.containsKey("ingredients") ? record.get("ingredients").asList(Value::asString) : new ArrayList<>());
        return detail;
    }

    private Company mapToCompany(Record record) {
        Company company = new Company();
        company.setName(getStringFromRecord(record, "product"));
        company.setLocation(getStringFromRecord(record, "location"));
        company.setCompany(getStringFromRecord(record, "company"));
        return company;
    }

    public List<CocoaProductDetail> executeProductDetailQuery(String query, Map<String, Object> params) {
        return executeQuery(query, params, this::mapToCocoaProductDetail);
    }

    public List<CocoaProduct> executeProductQuery(String query, Map<String, Object> params) {
        return executeQuery(query, params, this::mapToCocoaProduct);
    }

    public List<Company> executeCompanyQuery(String query, Map<String, Object> params) {
        return executeQuery(query, params, this::mapToCompany);
    }

    private <T> List<T> executeQuery(String query, Map<String, Object> params, Function<Record, T> mapper) {
        try (Session session = driver.session()) {
            return session.run(query, params).list(mapper);
        } catch (Exception e) {
            // Log and handle the exception appropriately
            throw new RuntimeException("Failed to execute query: " + query, e);
        }
    }
}