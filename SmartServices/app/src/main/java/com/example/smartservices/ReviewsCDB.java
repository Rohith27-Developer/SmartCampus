package com.example.smartservices;

import java.io.Serializable;

public class ReviewsCDB implements Serializable {
    String email,review,time,name;

    public ReviewsCDB() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ReviewsCDB(String email, String review, String time, String name) {
        this.email = email;
        this.review = review;
        this.time = time;
        this.name=name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
