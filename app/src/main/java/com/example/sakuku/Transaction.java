package com.example.sakuku;

public class Transaction {
    private final int id;
    private final String type;
    private final double amount;
    private final String date;
    private final String description;
    private final String category;

    public Transaction(int id, String type, double amount, String date, String description, String category) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }
}