package com.example.smartservices;

import java.io.Serializable;

public class ChatBotCDB implements Serializable
{
    private String email,issue,regno,mobno,name;

    public ChatBotCDB() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public String getMobno() {
        return mobno;
    }

    public void setMobno(String mobno) {
        this.mobno = mobno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChatBotCDB(String email, String issue, String regno, String mobno, String name) {
        this.email = email;
        this.issue = issue;
        this.regno = regno;
        this.mobno = mobno;
        this.name = name;
    }
}
