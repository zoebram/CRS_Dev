package com.fyp_cocoa.Cocoa_Recommendation_System.model;

import lombok.Data;
import lombok.Setter;

import java.util.List;

@Setter
@Data
public class CocoaProduct{
    private String id;
    private String name;
    private double cocoaPercent;
    private double rating;
    private int numberOfIngredients;
}
