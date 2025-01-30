package ru.finance_manager.database;

import com.google.gson.Gson;
import ru.finance_manager.database.models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static final String DATABASE_FILENAME = "database.json";
    private List<User> userDataBase = new ArrayList<>();
    private List<Operation> operationDataBase = new ArrayList<>();

    public Operation addOperation(NewOperation userOperation) {
        Operation newOperation = new Operation(
                operationDataBase.size() + 1,
                userOperation.getUser(),
                userOperation.getCategory(),
                userOperation.getAmount());
        operationDataBase.add(newOperation);
        return newOperation;
    }


    public ArrayList<Operation> getAllUserOperations(User user) {
        ArrayList<Operation> allUserOperations = new ArrayList<>();
        for (Operation link : operationDataBase) {
            if (link.getUser().getLogin().equals(user.getLogin())) {
                allUserOperations.add(link);
            }
        }
        return allUserOperations;
    }

    public User addUser(NewUser user) {
        User newUser = new User(userDataBase.size() + 1, user.getLogin(), user.getPassword(), user.getCategories());
        userDataBase.add(newUser);
        return newUser;
    }

    public User authUser(String login, String password) {
        for (User user : userDataBase) {
            if (user.getLogin().equals(login)) {
                if (user.checkByPassword(password)) {
                    return user;
                }
            }
        }
        return null;
    }

    public boolean checkLogin(String login) {
        for (User user : userDataBase) {
            if (user.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    public void updateUser(User curUser) {
        for (User user : userDataBase) {
            if (user.getId() == curUser.getId()) {
                userDataBase.set(userDataBase.indexOf(user), curUser);
            }
        }
    }

    public ArrayList<Category> getAllUserCategories(User user) {
        return user.getCategories();
    }


    public void saveToFile() {
        try (Writer writer = new FileWriter(DATABASE_FILENAME)) {
            Gson gson = new Gson();
            gson.toJson(this, writer);
        } catch (IOException e) {
            System.out.println("Ошибка сохранения в файл");
            e.printStackTrace();
        }
    }

    private static DataBase loadFromFile() {
        try {
            Reader reader = new FileReader(DATABASE_FILENAME);
            Gson gson = new Gson();
            return gson.fromJson(reader, DataBase.class);
        } catch (Exception e) {
//            System.out.println("Старая база не считана");
            return new DataBase();
        }
    }

    public static DataBase createDataBase() {
        return loadFromFile();
    }

}
