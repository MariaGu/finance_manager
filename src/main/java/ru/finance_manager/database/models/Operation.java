package ru.finance_manager.database.models;

public class Operation {
    private int id;
    private User user;
    private Category category;
    private double amount;


    public Operation(int id, User user, Category category, double amount) {
        this.id = id;
        this.user = user;
        this.category = category;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Category getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

}
