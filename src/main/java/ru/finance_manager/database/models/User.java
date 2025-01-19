package ru.finance_manager.database.models;

import java.util.ArrayList;
import java.util.Objects;

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

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}
