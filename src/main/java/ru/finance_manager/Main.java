package ru.finance_manager;


import ru.finance_manager.database.DataBase;
import ru.finance_manager.database.models.NewUser;
import ru.finance_manager.database.models.User;
import ru.finance_manager.utils.PasswordHasher;
import ru.finance_manager.utils.WalletCalculator;
import ru.finance_manager.utils.WalletHandler;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        DataBase dataBase = DataBase.createDataBase();
        Scanner scanner = new Scanner(System.in);

        printWelcomeMenu();
        while (true) {
            String s = scanner.nextLine();
            if (s.equals("2")) {
                System.out.println("Завершение программы");
                break;
            } else if (s.equals("0")) {
                User user = login(scanner, dataBase);
                if (user == null) {
                } else {
                    enterWallet(scanner, dataBase, user);
                }
            } else if (s.equals("1")) {
                User user = register(scanner, dataBase);
                enterWallet(scanner, dataBase, user);
            } else {
                System.out.println("Операция не поддерживается");
                printWelcomeMenu();
            }
        }
        scanner.close();
    }

    private static void printWelcomeMenu() {
        System.out.println("""
                Перед Вами Система управления личными финансами!
                Введите номер пункта меню для выбора действия:
                0 -> Войти в систему по логину и паролю
                1 -> Зарегистрироваться в системе
                2 -> Завершить программу
                """);
    }

    private static User login(Scanner scanner, DataBase dataBase) {
        System.out.println("Введите ваш логин:");
        String curLogin = scanner.nextLine();
        if (dataBase.checkLogin(curLogin)) {
            System.out.println("Введите ваш пароль:");
            String curPassword = scanner.nextLine();
            String hashedPass = PasswordHasher.hashPassword(curPassword);
            User curUser = dataBase.authUser(curLogin, hashedPass);
            if (curUser == null) {
                System.out.println("Вход в систему не удался: неверный пароль");
                return null;
            } else {
                System.out.println("Вы успешно вошли в систему! Переход в личный кошелек...");
                return curUser;
            }
        } else {
            System.out.println("Вход в систему не удался: логин не найден");
            return null;
        }
    }

    private static User register(Scanner scanner, DataBase dataBase) {
        System.out.println("Введите ваш логин:");
        String curLogin = scanner.nextLine();
        System.out.println("Введите ваш пароль:");
        String curPassword = scanner.nextLine();
        String hashedPass = PasswordHasher.hashPassword(curPassword);
        User user = dataBase.addUser(new NewUser(curLogin, hashedPass));
        System.out.println("Регистрация прошла успешно! Переход в личный кошелек...");
        return user;
    }

    private static void printWalletMenu() {
        System.out.println("""
                   Добро пожаловать в личный кошелек!
                   Введите номер пункта меню для выбора действия:
                1 -> Добавить операцию;
                2 -> Управлять категориями;
                3 -> Вывести общую сумму доходов/расходов;
                4 -> Отобразить детализацию по всем операциям;
                5 -> Выполнить подсчёт по выбранным категориям;

                6 -> Выйти из личного кошелька
                """);
    }

    private static void enterWallet(Scanner scanner, DataBase dataBase, User user) {
        printWalletMenu();
        String userChoice = scanner.nextLine();

        switch (userChoice) {
            case "6": {
                System.out.println("Вы успешно вышли из электронного кошелька'!");
                break;
            }

            case "1": {
                WalletHandler.addOperation(dataBase, user, scanner);
                break;
            }
            case "2": {
                WalletHandler.editCategoryGateway(dataBase, user, scanner);
                break;
            }
            case "3": {
                WalletCalculator.printTotalBudgetAmount(dataBase, user);
                break;
            }
            case "4": {
                WalletCalculator.printDetailAmount(dataBase, user);
                break;
            }
            case "5": {
                WalletCalculator.printCalculationByCategory(dataBase, user);
                break;
            }
            default:
                System.out.println("Вы ввели неверную команду - попробуйте ещё раз.");
                printWalletMenu();
                break;
        }

    }
}