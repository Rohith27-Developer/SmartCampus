package com.example.smartservices;

public class BusUpdatesdb {
    private String text,date;

    public BusUpdatesdb(String text, String date) {
        this.text = text;
        this.date = date;
    }

    public BusUpdatesdb() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
