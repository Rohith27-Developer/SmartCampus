package com.example.smartservices;

public class Ordersdb {
    private String items;
    private String quantity;
    private String image;
    private String status;
    private String datetime;
    private String category;
    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Ordersdb(String name, String orderno, String email) {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Ordersdb(String items, String quantity, String image, String email, String orderno) {
        this.items = items;
        this.quantity = quantity;
        this.image = image;
        this.email = email;
        this.orderno=orderno;
    }
    public Ordersdb(String items, String quantity, String image, String email, String orderno,String category,String datetime) {
        this.items = items;
        this.quantity = quantity;
        this.image = image;
        this.email = email;
        this.orderno=orderno;
        this.category=category;
        this.datetime=datetime;
    }
    public Ordersdb(String items, String quantity, String image, String email, String orderno,String status,String datetime,String category) {
        this.items = items;
        this.quantity = quantity;
        this.image = image;
        this.email = email;
        this.orderno=orderno;
        this.status=status;
        this.datetime=datetime;
        this.category=category;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    private String orderno;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;
    public Ordersdb()
    {

    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
