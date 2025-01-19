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
    private List<Category> categoriesDataBase = new ArrayList<>();


    //    Operations
    public Operation addOperation(NewOperation userOperation) {
        int id = 1;
        if (!operationDataBase.isEmpty()) {
            id = operationDataBase.get(operationDataBase.size() - 1).getId() + 1;
        }
        Operation newOperation = new Operation(id, userOperation.getUser(), userOperation.getCategory(), userOperation.getAmount());
        operationDataBase.add(newOperation);
        saveToFile();
        return newOperation;
    }

    public void updateOperation(Operation userOperation) {
        for (Operation operation : operationDataBase) {
            if (Objects.equals(operation.getId(), userOperation.getId())) {
                int indexToReplace = operationDataBase.indexOf(operation);
                operationDataBase.set(indexToReplace, userOperation);
            }
        }
        saveToFile();
    }

    public Operation getOperationByID(int operationID) {
        for (Operation operation : operationDataBase) {
            if (Objects.equals(operation.getId(), operationID)) return operation;
        }
        return null;
    }

    public boolean removeOperationByID(int operationID) {
        for (Operation operation : operationDataBase) {
            if (Objects.equals(operation.getId(), operationID)) {
                operationDataBase.remove(operation);
                saveToFile();
                return true;
            }
        }
        saveToFile();
        return false;
    }

    public ArrayList<Operation> getAllUserOperations(User user) {
        ArrayList<Operation> outList = new ArrayList<>();
        for (Operation link : operationDataBase) {
            if (Objects.equals(link.getUser().getLogin(), user.getLogin())) {
                outList.add(link);
            }
        }
        return outList;
    }

    //    Users
    public User addUser(NewUser user) {
        int id = 1;
        if (!userDataBase.isEmpty()) {
            id = userDataBase.get(userDataBase.size() - 1).getId() + 1;
        }
        User newUser = new User(id, user.getLogin(), user.getPassword(), user.getCategories());
        userDataBase.add(newUser);
        saveToFile();
        return newUser;
    }

    public User authUser(String login, String password) {
        for (User user : userDataBase) {
            if (Objects.equals(user.getLogin(), login)) {
                if (user.checkByPassword(password)) {
                    return user;
                }
            }
        }
        return null;
    }

    public boolean checkLogin(String login) {
        for (User user : userDataBase) {
            if (Objects.equals(user.getLogin(), login)) {
                return true;
            }
        }
        return false;
    }

    public void updateUser(User curUser) {
        for (User user : userDataBase) {
            if (Objects.equals(user.getId(), curUser.getId())) {
                int indexToReplace = userDataBase.indexOf(user);
                userDataBase.set(indexToReplace, curUser);
            }
        }
        saveToFile();
    }

    public boolean transferToUserByLogin(String login, float amount) {
        User curUser = null;
        for (User user : userDataBase) {
            if (Objects.equals(user.getLogin(), login)) {
                curUser = user;
                break;
            }
        }
        if (curUser == null) return false;
        Category transferInCategory = null;
        for (Category cat : curUser.getCategories()) {
            if (Objects.equals(cat.getName(), "Входящие переводы") && cat.isProfit()) {
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
            e.printStackTrace();
            return new DataBase();
        }
    }

    public static DataBase createDataBase() {
        return loadFromFile();
    }

}
