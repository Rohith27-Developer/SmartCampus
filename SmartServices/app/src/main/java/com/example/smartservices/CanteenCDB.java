package com.example.smartservices;

import java.io.Serializable;

public class CanteenCDB
{
    private String price;
    private String name;
    private String calories;
    private String carbs;
    private String prots;
    private String fats;
    private String category;
    private String status;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CanteenCDB(String name, String status, Double rating, String image) {
        this.name = name;
        this.status = status;
        this.rating = rating;
        this.image = image;
    }

    public CanteenCDB(String name, String price, String category, String calories, String carbs, String prots, String fats, String image) {
        this.price = price;
        this.name = name;
        this.calories = calories;
        this.carbs = carbs;
        this.prots = prots;
        this.fats = fats;
        this.category = category;
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getProts() {
        return prots;
    }

    public void setProts(String prots) {
        this.prots = prots;
    }

    public String getFats() {
        return fats;
    }

    public void setFats(String fats) {
        this.fats = fats;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    private Double rating;

    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CanteenCDB()
    {


    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }
}


