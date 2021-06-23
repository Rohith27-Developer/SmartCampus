package com.example.smartservices;

import java.io.Serializable;

public class Securitycdb implements Serializable {
    public Securitycdb() {
    }
    private String roomno,status;

    public Securitycdb(String roomno, String status) {
        this.roomno = roomno;
        this.status = status;
    }

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
