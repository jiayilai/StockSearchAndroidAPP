package com.example.hw9.myfirstapp;

/**
 * Created by yangyidong on 4/17/16.
 */
public class Product {

    private String productId;
    private String name;
    private String description;
    private Double price;

    public String getProdcutId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }

    public Product( String productId, String name, String description, Double price){
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
    }

}
