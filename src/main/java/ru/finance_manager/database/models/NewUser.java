package ru.finance_manager.database.models;

import java.util.ArrayList;

public class NewUser {
    private String login;
    private String password;
    private ArrayList<Category> categories;


    public NewUser(String login, String password) {
        this.login = login;
        this.password = password;
        this.categories = new ArrayList<>();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }
}
