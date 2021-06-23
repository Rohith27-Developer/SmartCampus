package com.example.smartservices;

public class Book {
    private String Title;
    private String Price ;
    private String Status ;
    private String Thumbnail ;
    private String carbs;
    private String prots;
    private String fats;
    private String cals;

    public String getCals() {
        return cals;
    }

    public void setCals(String cals) {
        this.cals = cals;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Book() {
    }

    public Book(String title, String price, String status, String thumbnail,String carb,String prot,String fat,String cal) {
        Title = title;
        Price = price;
        Thumbnail = thumbnail;
        Status=status;
        carbs=carb;
        prots=prot;
        fats=fat;
        cals=cal;
    }
    public Book(String title, String price,String thumbnail) {
        Title = title;
        Price = price;
        Thumbnail = thumbnail;

    }

    public String getTitle() {
        return Title;
    }



    public String getThumbnail() {
        return Thumbnail;
    }


    public void setTitle(String title) {
        Title = title;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }



    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }
}

