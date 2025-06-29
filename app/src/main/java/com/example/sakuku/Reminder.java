package com.example.sakuku;

public class Reminder {
    private int id;
    private String name;
    private String date;
    private String time;
    private String status;
    private double amount;

    public Reminder(int id, String name, String date, String time, String status, double amount) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.status = status;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public double getAmount() {
        return amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}