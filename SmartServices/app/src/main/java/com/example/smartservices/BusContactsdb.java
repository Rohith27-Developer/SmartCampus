package com.example.smartservices;

public class BusContactsdb {
    private String busno,phone,email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BusContactsdb() {
    }

    public BusContactsdb(String busno, String phone) {
        this.busno = busno;
        this.phone = phone;
    }

    public String getBusno() {
        return busno;
    }

    public void setBusno(String busno) {
        this.busno = busno;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
