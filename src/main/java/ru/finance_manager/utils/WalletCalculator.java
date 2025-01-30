package ru.finance_manager.utils;

import ru.finance_manager.database.DataBase;
import ru.finance_manager.database.models.Category;
import ru.finance_manager.database.models.Operation;
import ru.finance_manager.database.models.User;

import java.util.*;

public class WalletCalculator {

    public static void printTotalBudgetAmount(DataBase DataBase, User user) {
        double profit = 0;
        double notProfit = 0;
        for (Operation operation : DataBase.getAllUserOperations(user)) {
            if (operation.getCategory().isProfit()) {
                profit += operation.getAmount();
            } else {
                notProfit += operation.getAmount();
            }
        }
        System.out.println("Анализ вашего бюджета:\n" +
                "Доходы:" + profit +
                "\nРасходы: "+ notProfit +
                "\nОстаток: " + (profit - notProfit));

    }
    public static void printDetailAmount(DataBase dataBase, User user, Scanner scanner) {
        double profit = 0;
        double notProfit = 0;
        HashMap<Category, Double> profitCategories = new HashMap<>();
        HashMap<Category, Double> nonProfitCategories = new HashMap<>();
        for (Operation operation : dataBase.getAllUserOperations(user)) {
            if (operation.getCategory().isProfit()) {
                profit += operation.getAmount();
                Double curOpAmount = profitCategories.get(operation.getCategory());
                if (curOpAmount == null) {
                    profitCategories.put(operation.getCategory(), operation.getAmount());
                } else {
                    curOpAmount += operation.getAmount();
                    profitCategories.put(operation.getCategory(), curOpAmount);
                }

            } else {
                notProfit += operation.getAmount();
                Double curOpAmount = nonProfitCategories.get(operation.getCategory());
                if (curOpAmount == null) {
                    nonProfitCategories.put(operation.getCategory(), operation.getAmount());
                } else {
                    curOpAmount += operation.getAmount();
                    nonProfitCategories.put(operation.getCategory(), curOpAmount);
                }

            }
        }
        StringBuilder profitString = new StringBuilder();
        StringBuilder nonProfitString = new StringBuilder();

        for (Map.Entry<Category, Double> entry : profitCategories.entrySet()) {
            profitString.append(entry.getKey().getName()).append(": ").append(entry.getValue()).append("\n");
        }
        for (Map.Entry<Category, Double> entry : nonProfitCategories.entrySet()) {
            nonProfitString.append(entry.getKey().getName()).append(": ").append(entry.getValue()).append(", Оставшийся бюджет: ").append(entry.getKey().getQuota()-entry.getValue()).append("\n");
        }

        System.out.println("Анализ вашего бюджета:\n" +
                "Общий доход:" + profit +
                "\nДоходы по категориям: \n" + profitString +
                "\n\nОбщие расходы: "+ notProfit +
                "\nБюджет по категориям: \n" + nonProfitString +
                "\n\nОстаток: " + (profit - notProfit));
    }

    public static void printCalculationByCategory(DataBase DataBase, User user, Scanner scanner) {
        System.out.println("Введите номер интересующей вас категории (при выборе нескольких - перечислите через запятую):");
        for (Category category: user.getCategories()) {
            System.out.println(user.getCategories().indexOf(category) + " -> " + category.getName() + ". Лимит категории: " + category.getQuota() + ".\n");
        }
        String userInput = scanner.nextLine();
        ArrayList<Category> selectedCategories = new ArrayList<>();
        String[] selectedCategoriesString = userInput.replace(" ", "").split(",");
        try {
            for (String str: selectedCategoriesString) {
                selectedCategories.add(user.getCategories().get(Integer.parseInt(str)));
            }
        } catch (Exception e) {
            System.out.println("Похоже, вы ввели неверное число, попробуйте ещё раз.");
            return;
        }
        ArrayList<Integer> selectedCategoriesID = new ArrayList<>();
        for (Category cat: selectedCategories) {
            selectedCategoriesID.add(cat.getId());
        }
        double profit = 0;
        double notProfit = 0;
        HashMap<Category, Double> profitCategories = new HashMap<>();
        HashMap<Category, Double> nonProfitCategories = new HashMap<>();
        for (Operation operation : DataBase.getAllUserOperations(user)) {
            if (selectedCategoriesID.contains(operation.getCategory().getId())) {
                if (operation.getCategory().isProfit()) {
                    profit += operation.getAmount();
                    Double curOpAmount = profitCategories.get(operation.getCategory());
                    if (curOpAmount == null) {
                        profitCategories.put(operation.getCategory(), operation.getAmount());
                    } else {
                        curOpAmount += operation.getAmount();
                        profitCategories.put(operation.getCategory(), curOpAmount);
                    }

                } else {
                    notProfit += operation.getAmount();
                    Double curOpAmount = nonProfitCategories.get(operation.getCategory());
                    if (curOpAmount == null) {
                        nonProfitCategories.put(operation.getCategory(), operation.getAmount());
                    } else {
                        curOpAmount += operation.getAmount();
                        nonProfitCategories.put(operation.getCategory(), curOpAmount);
                    }

                }
            }
        }
        StringBuilder profitString = new StringBuilder();
        StringBuilder nonProfitString = new StringBuilder();

        for (Map.Entry<Category, Double> entry : profitCategories.entrySet()) {
            profitString.append(entry.getKey().getName()).append(": ").append(entry.getValue()).append("\n");
        }
        for (Map.Entry<Category, Double> entry : nonProfitCategories.entrySet()) {
            nonProfitString.append(entry.getKey().getName()).append(": ").append(entry.getValue()).append(", Оставшийся бюджет: ").append(entry.getKey().getQuota()-entry.getValue()).append("\n");
        }
        if (profit!= 0 && notProfit != 0) {
            System.out.println("Анализ вашего бюджета по выбранным категориям:\n" +
                    "Общий доход по выбранным категориям:" + profit +
                    "\nДоходы по категориям: \n" + profitString +
                    "\n\nОбщие расходы по выбранным категориям: "+ notProfit +
                    "\nБюджет по категориям: \n" + nonProfitString +
                    "\n\nОстаток: " + (profit - notProfit));
        } else if (profit!=0) {
            System.out.println("Анализ вашего бюджета по выбранным категориям:\n" +
                    "Общий доход по выбранным категориям:" + profit +
                    "\nДоходы по категориям: \n" + profitString);
        } else if (notProfit != 0) {
            System.out.println("Анализ вашего бюджета по выбранным категориям:\n" +
                    "\nОбщие расходы по выбранным категориям: "+ notProfit +
                    "\nБюджет по категориям: \n" + nonProfitString);

        } else {
            System.out.println("Не найдено операций по выбранным категориям");
        }
    }
}

