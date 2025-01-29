package ru.finance_manager.database;

import com.google.gson.Gson;
import ru.finance_manager.database.models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        saveToFile();
        return newOperation;
    }


    public ArrayList<Operation> getAllUserOperations(User user) {
        ArrayList<Operation> outList = new ArrayList<>();
        for (Operation link : operationDataBase) {
            if (link.getUser().getLogin().equals(user.getLogin())) {
                outList.add(link);
            }
        }
        return outList;
    }

    public User addUser(NewUser user) {
        User newUser = new User(userDataBase.size() + 1, user.getLogin(), user.getPassword(), user.getCategories());
        userDataBase.add(newUser);
        saveToFile();
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
            if (user.getId()== curUser.getId()) {
                int indexToReplace = userDataBase.indexOf(user);
                userDataBase.set(indexToReplace, curUser);
            }
        }
        saveToFile();
    }

    public boolean transferToUserByLogin(String login, double amount) {
        User curUser = null;
        for (User user : userDataBase) {
            if (user.getLogin().equals(login)) {
                curUser = user;
                break;
            }
        }
        if (curUser == null) return false;
        Category transferInCategory = null;
        for (Category cat : curUser.getCategories()) {
            if (cat.getName().equals("Входящие переводы") && cat.isProfit()) {
                transferInCategory = cat;
                break;
            }
        }
        if (transferInCategory == null) {

            float newQuota = 0;
            int newId = 1;
            if (!curUser.getCategories().isEmpty()) {
                newId = curUser.getCategories().get(curUser.getCategories().size() - 1).getId() + 1;
            }
            Category newCategory = new Category(newId, "Входящие переводы", newQuota, true);
            curUser.getCategories().add(newCategory);
            transferInCategory = newCategory;
            updateUser(curUser);
        }
        NewOperation incomeOp = new NewOperation(curUser, transferInCategory, amount);
        addOperation(incomeOp);
        saveToFile();
        return true;

    }

    public ArrayList<Category> getAllUserCategories(User user) {
        return user.getCategories();
    }


    private void saveToFile() {
        try (Writer writer = new FileWriter(DATABASE_FILENAME)) {
            Gson gson = new Gson();
            gson.toJson(this, writer);
        } catch (IOException e) {
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
