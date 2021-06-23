package com.example.smartservices;

public class Note
{
    private String link;
    private  String desc;
    private  String title;
    private  String month;
    private  String day;
    public Note()
    {


    }
    public Note(String link, String desc, String title)
    {
        this.link=link;
        this.desc=desc;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}

