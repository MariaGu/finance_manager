package ru.finance_manager.database.models;

public class NewOperation {
    private User user;
    private Category category;
    private double amount;


    public NewOperation(User user, Category category, double amount) {
        this.user = user;
        this.category = category;
        this.amount = amount;
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
