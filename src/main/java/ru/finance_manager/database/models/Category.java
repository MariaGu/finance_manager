package ru.finance_manager.database.models;

public class Category {
    private int id;
    private String name;
    private double quota;
    private boolean isProfit;


    public Category(int id, String name, double quota, boolean isProfit) {
        this.id = id;
        this.name = name;
        this.quota = quota;
        this.isProfit = isProfit;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getQuota() {
        return quota;
    }

    public void setQuota(double quota) {
        this.quota = quota;
    }

    public boolean isProfit() {
        return isProfit;
    }

}
