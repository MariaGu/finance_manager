package ru.finance_manager.utils;

import ru.finance_manager.database.DataBase;
import ru.finance_manager.database.models.Category;
import ru.finance_manager.database.models.NewOperation;
import ru.finance_manager.database.models.User;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class WalletHandler {

    public static User addOperation(DataBase DataBase, User user, Scanner scanner) {
        ArrayList<Category> userCategories = DataBase.getAllUserCategories(user);
        if (userCategories.isEmpty()) {
            System.out.println("Создайте категорию операции");
        }
        System.out.println("""
                            Введите тип добавляемой операции:
                            1 -> Доход
                            2 -> Расход""");
        boolean isProfit = true;
        boolean isCategoryExists = false;
        ArrayList<Category> currentCategories = new ArrayList<>();
        String userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            for (Category category: userCategories) {
                if (category.isProfit()) {
                    currentCategories.add(category);
                }
            }
        } else if (userInput.equals("2")) {
            isProfit = false;
            for (Category category: userCategories) {
                if (!category.isProfit()) {
                    currentCategories.add(category);
                }
            }

        } else {
            System.out.println("Вы ввели неверную команду, попробуйте ещё раз.");
            return user;
        }
        if (currentCategories.isEmpty()) {
            if (isProfit) {
                System.out.println("Сначала вам нужно создать категорию операции дохода");
                return user;
            } else {
                System.out.println("Сначала вам нужно создать категорию операции расхода");
                return user;
            }
        }
        System.out.println("Введите сумму операции:");
        String sumType = scanner.nextLine();
        double amount = 0;
        try {
            amount = Double.parseDouble(sumType);
        } catch (NumberFormatException e) {
            System.out.println("Вы ввели неверное число, попробуйте ещё раз.");
            return user;
        }
        System.out.println("Введите номер категории операции. Ваши категории:");
        for (Category category: currentCategories) {
            System.out.println(currentCategories.indexOf(category) + " -> " + category.getName()
                    + ". Лимит категории: " + category.getQuota() + ".\n");
        }
        try {
            Category curCategory = currentCategories.get(Integer.parseInt(scanner.nextLine()));
            DataBase.addOperation(new NewOperation(user, curCategory, amount));
            System.out.println("Операция успешно добавлена!");
        } catch (Exception e) {
            System.out.println("Вы ввели неверное число, попробуйте ещё раз.");
            return user;
        }
        return user;
    }

    public static User editCategoryGateway(DataBase DataBase, User user, Scanner scanner) {
        User refreshUser = user;
        System.out.println("""
                Введите, что вы хотите сделать:
                1 -> Добавить новую категорию
                2 -> Изменить бюджет на категорию""");
        switch (scanner.nextLine()) {
            case "1" : {
                refreshUser = addCategory(DataBase, user, scanner);
                break;
            }
            case "2" : {
                refreshUser = editCategoryQuota(DataBase, user, scanner);
                break;
            }
            default:
                System.out.println("Вы ввели неверное число, попробуйте ещё раз."); break;
        }
        return refreshUser;
    }

    private static User addCategory(DataBase DataBase, User user, Scanner scanner) {
        System.out.println("Введите название новой категории:");
        String newName = scanner.nextLine();
        System.out.println("""
                Введите тип категории:
                1 -> Доходы
                2 -> Расходы""");
        boolean newIsProfit = false;
        String userIsProfit = scanner.nextLine();
        if (userIsProfit.equals("1")) newIsProfit = true;
        else if (userIsProfit.equals("2")) {}
        else {
            System.out.println("Вы ввели неверное значение, попробуйте ещё раз.");
            return user;
        }
        System.out.println("Введите бюджет на данную категорию:");
        double newQuota = 0;
        try {
            newQuota = Double.parseDouble(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Вы ввели неверное число, попробуйте ещё раз.");
            return user;
        }
        int newId = 1;
        if (!user.getCategories().isEmpty()) {
            newId = user.getCategories().get(user.getCategories().size()-1).getId() + 1;
        }
        Category newCategory = new Category(
                newId,
                newName,
                newQuota,
                newIsProfit
        );
        user.getCategories().add(newCategory);
        DataBase.updateUser(user);
        System.out.println("Категория " + newCategory.getName() + " успешно добавлена!");
        return user;
    }
    private static User editCategoryQuota(DataBase DataBase, User user, Scanner scanner) {
        ArrayList<Category> userCategories = DataBase.getAllUserCategories(user);
        StringBuilder textCategories = new StringBuilder("Введите номер категории операции. Ваши категории:\n");
        for (Category category: userCategories) {
            textCategories.append(userCategories.indexOf(category)).append(" -> ")
                    .append(category.getName()).append(". Лимит категории: ").append(category.getQuota()).append(".\n");
        }
        System.out.println(textCategories);
        try {
            Category curCategory = userCategories.get(Integer.parseInt(scanner.nextLine()));
            System.out.println("Текущий бюджет на категорию: " + curCategory.getQuota() + ". \n" +
                    "Введите новое значение бюджета либо оставьте пустым для отмены изменений:");
            try {
                String userInput = scanner.nextLine();
                if (userInput.isEmpty()) {
                    System.out.println("Бюджет категории остался без изменений.");
                } else {
                    user.getCategories().get(userCategories.indexOf(curCategory)).setQuota(Double.parseDouble(userInput));
                    DataBase.updateUser(user);
                    System.out.println("Бюджет на категорию " + curCategory.getName() + " успешно изменён. Новый бюджет: "
                            + user.getCategories().get(userCategories.indexOf(curCategory)).getQuota());

                }
            } catch (Exception e) {
                System.out.println("Вы ввели неверное значение, попробуйте ещё раз.");
                return user;
            }
        } catch (Exception e) {
            System.out.println("Вы ввели неверное число, попробуйте ещё раз.");
            return user;
        }
        return user;
    }

    public static User transferMoney(DataBase DataBase, User user, Scanner scanner) {
        Category transferOutCategory = null;
        float newQuota = 0;
        for (Category cat : user.getCategories()) {
            if (Objects.equals(cat.getName(), "Исходящие переводы") && !cat.isProfit()) {
                transferOutCategory = cat;
                break;
            }
        }
        if (transferOutCategory == null) {
            System.out.println("Будет создана категория расходов <Исходящие переводы>. Введите бюджет на данную категорию:");
            String userInput = scanner.nextLine();
            try {
                newQuota = Float.parseFloat(userInput);
                int newId = 1;
                if (!user.getCategories().isEmpty()) {
                    newId = user.getCategories().get(user.getCategories().size()-1).getId() + 1;
                }
                Category newCategory = new Category(
                        newId,
                        "Исходящие переводы",
                        newQuota,
                        false
                );
                user.getCategories().add(newCategory);
                DataBase.updateUser(user);
            } catch (Exception e) {
                System.out.println("Вы ввели неверное значение, попробуйте ещё раз.");
            }
        }

        System.out.println("Введите логин получателя перевода:");
        String reseiverLogin = scanner.nextLine();
        if (DataBase.checkLogin(reseiverLogin)) {
            System.out.println("Получатель найден, введите сумму для перевода:");
        } else {
            System.out.println("Получатель не найден, попробуйте снова.");
            return user;
        }
        String sumToSend = scanner.nextLine();
        double amountToSend = 0;
        try {
            if (Objects.equals(sumToSend, "0")) {
                System.out.println("Сумма перевода равна 0, операция не будет выполнена. Возврат в главное меню...");
                return user;
            } else {
                amountToSend = Double.parseDouble(sumToSend);
            }
        } catch (Exception e) {
            System.out.println("Вы ввели неверное значение, попробуйте ещё раз.");
            return user;
        }

        if (amountToSend == 0) {
            System.out.println("Вы ввели неверное значение, попробуйте ещё раз.");
        } else {
            boolean isSent = DataBase.transferToUserByLogin(reseiverLogin, amountToSend);
            if (isSent) {
                DataBase.addOperation(new NewOperation(user, transferOutCategory, amountToSend));
                System.out.println("Операция успешно выполнена! Отправлено " + amountToSend + " пользователю " + reseiverLogin);
            }
        }
        return user;
    }

}

