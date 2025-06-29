package com.example.sakuku;

public class CategoryStat {
    private String categoryName;
    private int percentage;

    public CategoryStat(String categoryName, int percentage) {
        this.categoryName = categoryName;
        this.percentage = percentage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getPercentage() {
        return percentage;
    }
}