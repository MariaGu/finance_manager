package ru.finance_manager;


import ru.finance_manager.database.DataBase;
import ru.finance_manager.database.models.NewUser;
import ru.finance_manager.database.models.User;
import ru.finance_manager.utils.PasswordHasher;
import ru.finance_manager.utils.BudgetCalculator;

import java.util.Objects;
import java.util.Scanner;

import static ru.finance_manager.utils.BudgetHandler.*;

public class Main {
    private static User currentUser = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DataBase dataBase = DataBase.createDataBase();
        String outText = "";
        System.out.println("\n\n\n");
        if (Objects.equals(currentUser, null)) {
            outText = ("""
                    Перед Вами Система управления личными финансами!
                    Отправьте короткую ссылку для перехода на исходный ресурс либо введите номер пункта меню для выбора действия:
                                        
                    00 -> Войти в систему по логину и паролю
                    01 -> Зарегистрироваться в системе
                    # -> Завершить программу
                    """);
        } else outText = String.format("""
                %s, добро пожаловать в систему управления личными финансами!
                Отправьте короткую ссылку для перехода на исходный ресурс либо введите номер пункта меню для выбора действия:
                1 -> Добавить операцию
                2 -> Управлять категориями
                3 -> Вывести общую сумму доходов/расходов
                4 -> Отобразить детализацию по всем операциям
                5 -> Выполнить подсчёт по выбранным категориям
                6 -> Перевести деньги другому пользователю
                                
                0 -> Выйти из системы
                # -> Завершить программу
                """, currentUser.getLogin());
        System.out.println(outText);
        String userChoice = scanner.nextLine();

        switch (userChoice) {
            //выход
            case "0": {
                currentUser = null;
                System.out.println("Вы успешно вышли из системы!");
                break;
            }
            //вход
            case "00": {
                System.out.println("Введите ваш логин:");
                String curLogin = scanner.nextLine();
                if (dataBase.checkLogin(curLogin)) {
                    System.out.println("Введите ваш пароль:");
                    String curPassword = scanner.nextLine();
                    String hashedPass = PasswordHasher.hashPassword(curPassword);
                    User curUser = dataBase.authUser(curLogin, hashedPass);
                    if (curUser == null) {
                        System.out.println("Вход в систему не удался: неверный пароль");
                    } else {
                        currentUser = curUser;
                        System.out.println("Вы успешно вошли в систему!");
                    }
                } else {
                    System.out.println("Вход в систему не удался: логин не найден");
                }
                break;
            }
            //регистрация
            case "01": {
                System.out.println("Введите ваш логин:");
                String curLogin = scanner.nextLine();
                System.out.println("Введите ваш пароль:");
                String curPassword = scanner.nextLine();
                String hashedPass = PasswordHasher.hashPassword(curPassword);

                currentUser = dataBase.addUser(new NewUser(curLogin, hashedPass));
                System.out.println("Регистрация прошла успешно! Возврат в главное меню...\n");
                break;
            }
            //Добавить операцию
            case "1": {
                if (currentUser == null) {
                    System.out.println("Для использования данной функции необходимо войти в систему.");
                } else {
                    currentUser = addOperation(dataBase, currentUser);
                }
                break;
            }
            //Управлять категориями
            case "2": {
                if (currentUser == null) {
                    System.out.println("Для использования данной функции необходимо войти в систему.");
                } else {
                    currentUser = editCategoryGateway(dataBase, currentUser);
                }
                break;
            }
            //Вывести общую сумму доходов/расходов
            case "3": {
                if (currentUser == null) {
                    System.out.println("Для использования данной функции необходимо войти в систему.");
                } else {
                    BudgetCalculator.printTotalBudgetAmount(dataBase, currentUser);
                }
                break;
            }
            //Отобразить детализацию по всем операциям
            case "4": {
                if (currentUser == null) {
                    System.out.println("Для использования данной функции необходимо войти в систему.");
                } else {
                    BudgetCalculator.printDetailAmount(dataBase, currentUser);
                }
                break;
            }
            //Выполнить подсчёт по выбранным категориям
            case "5": {
                if (currentUser == null) {
                    System.out.println("Для использования данной функции необходимо войти в систему.");
                } else {
                    BudgetCalculator.printCalculationByCategory(dataBase, currentUser);
                }
                break;
            }
            //Перевести деньги другому пользователю
            case "6": {
                if (currentUser == null) {
                    System.out.println("Для использования данной функции необходимо войти в систему.");
                } else {
                    currentUser = transferMoney(dataBase, currentUser);
                }
                break;
            }
            //завершение
            case "#": {
                System.out.flush();
                System.out.println("Завершение программы.");
                return;

            }
            default:
                System.out.println("Вы ввели неверную команду - попробуйте ещё раз.");
                break;
        }
        main(args);
    }


}