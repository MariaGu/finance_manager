package ru.finance_manager.database.models;

import java.util.ArrayList;

public class User {
    private int id;
    private String login;
    private String password;
    private ArrayList<Category> categories;


    public User(int id, String login, String password, ArrayList<Category> categories) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.categories = categories;
    }

    public boolean checkByPassword(String curPassword) {
        return password.equals(curPassword);
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

}
