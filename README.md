# Finance Manager Service

## Описание
Консольное приложение для управления личными финансами (электронный кошелек). 
Приложение позволяет пользователям добавлять доходы и расходы, устанавливать бюджеты на категории и получать статистику по своим финансам.

Программа написана на Java и использует JSON для внутренненго хранения данных.

---

## Требования

- **JDK** версии 8+
- **Файлы**:
    - `database.json`: база данных ссылок (создаётся автоматически)

---

## Использование

Программа предоставляет меню с выбором действий, например:
1. Добавить операцию
2. Управлять категориями
3. Вывести общую сумму доходов/расходов
4. Отобразить детализацию по всем операциям
5. Выполнить подсчёт по выбранным категориям

Выберите действие, следуйте инструкциям и вводите необходимые данные.

---
## Зависимости

Проект использует:
- **Gson** для работы с JSON
- **JSON-Simple** для обработки базы данных