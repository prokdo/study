package ru.prokdo.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ru.prokdo.model.database.DataBase;
import ru.prokdo.model.database.access.AccessKey;
import ru.prokdo.model.database.operational.User;
import ru.prokdo.model.database.operational.util.OperationArgs;
import ru.prokdo.model.database.operational.util.OperationType;


public final class Terminal {
    private static DataBase dataBase = new DataBase(
        "/home/prokdo/Университет/Git/study/content/DP/lab1/info", 
        "/home/prokdo/Университет/Git/study/content/DP/lab1/resource");
    
    private static BufferedReader inputReader;

    private static User currentUser;

    private Terminal() {}

    public static void show() {
        System.out.println("Лабораторная работа №2 \"Дискреционная политика безопасности\"");
        System.out.println("Автор: ВПР32, Прокопенко Дмитрий");

        System.out.println();

        Terminal.showLoginScreen();
    }

    private static void showLoginScreen() {
        System.out.println("Вход в систему\n");

        String userName = null;
        while (true) {
            System.out.print("login: ");

            inputReader = new BufferedReader(new InputStreamReader(System.in));

            try { userName = inputReader.readLine(); } 
            catch (IOException exception) { userName = null; }

            if (userName == null || userName.isBlank() || userName.isEmpty()) {
                System.out.println();
                System.out.println("Неверный формат имени пользователя! Повторите ввод\n");
                try { 
                    inputReader.reset();
                    inputReader.close(); 
                }
                catch (Exception ignored) {}
                continue;
            }

            if (dataBase.getUserId(userName) == -1) {
                System.out.println();
                System.out.println("Пользователя с таким именем не существует! Повторите ввод\n");
                try { 
                    inputReader.reset();
                    inputReader.close(); 
                }
                catch (Exception ignored) {}
                continue;
            }

            currentUser = dataBase.getUserByName(userName);

            try { 
                inputReader.reset();
                inputReader.close(); 
            }
            catch (Exception ignored) {}

            System.out.println(String.format("Успешная авторизация под пользователем: %s\n", userName));

            break;
        }

        Terminal.showUserWelcomeScreen();
    }

    private static void showUserWelcomeScreen() {
        System.out.println(String.format("Добро пожаловать, %s!\n", currentUser.getName()));
        System.out.println("* Для просмотра доступных команд, напишите help");

        System.out.println();

        String userCommand = null;
        while (true) {
            System.out.print(String.format("%s > ", currentUser.getName()));

            inputReader = new BufferedReader(new InputStreamReader(System.in));

            try { userCommand = inputReader.readLine(); } 
            catch (IOException exception) { userCommand = null; }

            if (userCommand == null || userCommand.isBlank() || userCommand.isEmpty()) {
                System.out.println();
                System.out.println("Неверный формат команды! Повторите ввод\n");
                try { 
                    inputReader.reset();
                    inputReader.close(); 
                }
                catch (Exception ignored) {}
                continue;
            }

            switch (userCommand) {
                case "help" -> { Terminal.showHelpScreen(); continue; }
                case "access" -> { Terminal.showUserAccessScreen(); continue; }
                case "read" -> { Terminal.showReadScreen(); continue; }
                case "write" -> { Terminal.showWriteScreen(); continue; }
                case "delegate" -> { Terminal.showDelegateScreen(); continue; }
                case "exit" -> { Terminal.showExitScreen(); continue; }
                default -> { System.out.println("Команда не распознана! Повторите ввод\n"); continue; }
            }
        }
        
    }

    private static void showHelpScreen() {
        System.out.println("Список команд: ");
        System.out.println();
        System.out.println("help - показать список команд");
        System.out.println("access - показать список объектов системы и Ваш доступ к ним");
        System.out.println("read - перейти к чтению файла");
        System.out.println("write - перейти к записи в файл");
        System.out.println("delegate - перейти к передаче прав на файл");
        System.out.println("exit - выйти из системы");
        System.out.println();
        System.out.println();
    }

    private static void showUserAccessScreen() {
        System.out.println("Перечень Ваших прав (r - доступ на чтение, w - доступ на запись, d - доступ на передачу прав): ");

        String[] filesNames = dataBase.getFilesNames();
        AccessKey[] userAccessKeys = dataBase.getUserAccessKeys(currentUser.getName());
        for (int i = 0; i < filesNames.length; i++)
            System.out.println(String.format("%s: %s", filesNames[i], userAccessKeys[i]));

        System.out.println();
    }

    private static void showReadScreen() {
        System.out.println("Для совершения операции введите следующие аргументы: имя_файла ");

        String userCommand = null;
        while (true) {
            System.out.print(String.format("%s > read > ", currentUser.getName()));

            inputReader = new BufferedReader(new InputStreamReader(System.in));

            try { userCommand = inputReader.readLine(); } 
            catch (IOException exception) { userCommand = null; }

            if (userCommand == null || userCommand.isBlank() || userCommand.isEmpty()) {
                System.out.println();
                System.out.println("Неверный формат аргумента! Повторите ввод\n");
                try { 
                    inputReader.reset();
                    inputReader.close(); 
                }
                catch (Exception ignored) {}
                continue;
            }

            if (userCommand.equals("exit")) Terminal.showExitScreen();
           
            if (dataBase.getFileId(userCommand) != -1) {
                OperationArgs operationArgs = new OperationArgs(OperationType.READ, dataBase.getFileId(userCommand), dataBase.getUserId(currentUser.getName()), -1, null);
                String result = dataBase.evaluate(operationArgs);
                if (result.equals("@FAIL@")) { System.out.println("Доступ запрещен!\n"); break; }
                else { System.out.println(String.format("Прочтено из файла: %s\n", result)); break; }
            }
            else { System.out.println("Неверное имя файла! Повторите ввод\n"); continue; }
            
        
        }
    }

    private static void showWriteScreen() {
        System.out.println("Для совершения операции введите следующие аргументы: имя_файла \"входная строка\" ");

        String userCommand = null;
        while (true) {
            System.out.print(String.format("%s > write > ", currentUser.getName()));

            inputReader = new BufferedReader(new InputStreamReader(System.in));

            try { userCommand = inputReader.readLine(); } 
            catch (IOException exception) { userCommand = null; }

            if (userCommand == null || userCommand.isBlank() || userCommand.isEmpty()) {
                System.out.println();
                System.out.println("Неверный формат аргументов! Повторите ввод\n");
                try { 
                    inputReader.reset();
                    inputReader.close(); 
                }
                catch (Exception ignored) {}
                continue;
            }

            if (userCommand.equals("exit")) Terminal.showExitScreen();

            String[] args = userCommand.split(" ");
            if (args.length != 2) {
                System.out.println();
                System.out.println("Неверный формат аргументов! Повторите ввод\n");
                try { 
                    inputReader.reset();
                    inputReader.close(); 
                }
                catch (Exception ignored) {}
                continue;
            }

            if (dataBase.getFileId(args[0]) != -1) {
                OperationArgs operationArgs = new OperationArgs(OperationType.WRITE, dataBase.getFileId(userCommand), dataBase.getUserId(currentUser.getName()), -1, args[1]);
                String result = dataBase.evaluate(operationArgs);
                if (result.equals("@FAIL@")) { System.out.println("Доступ запрещен!\n"); break; }
                else { System.out.println("Операция произведена успешно!\n"); break; }
            }
            else { System.out.println("Неверное имя файла! Повторите ввод\n"); continue; }
        }
    }

    private static void showDelegateScreen() {
        System.out.println("Для совершения операции введите следующие аргументы: имя_файла имя_пользователя");

        String userCommand = null;
        while (true) {
            System.out.print(String.format("%s > delegate > ", currentUser.getName()));

            inputReader = new BufferedReader(new InputStreamReader(System.in));

            try { userCommand = inputReader.readLine(); } 
            catch (IOException exception) { userCommand = null; }

            if (userCommand == null || userCommand.isBlank() || userCommand.isEmpty()) {
                System.out.println();
                System.out.println("Неверный формат аргументов! Повторите ввод\n");
                try { 
                    inputReader.reset();
                    inputReader.close(); 
                }
                catch (Exception ignored) {}
                continue;
            }

            if (userCommand.equals("exit")) Terminal.showExitScreen();

            String[] args = userCommand.split(" ");
            if (args.length != 2) {
                System.out.println();
                System.out.println("Неверный формат аргументов! Повторите ввод\n");
                try { 
                    inputReader.reset();
                    inputReader.close(); 
                }
                catch (Exception ignored) {}
                continue;
            }

            if (dataBase.getFileId(args[0]) != -1 && dataBase.getUserId(args[1]) != -1) {
                OperationArgs operationArgs = new OperationArgs(OperationType.DELEGATE, dataBase.getFileId(userCommand), dataBase.getUserId(currentUser.getName()), dataBase.getUserId(args[1]), null);
                String result = dataBase.evaluate(operationArgs);
                if (result.equals("@FAIL@")) { System.out.println("Доступ запрещен!\n"); break; }
                else { System.out.println(String.format("Операция произведена успешно - права на файл %s переданы пользователю %s\n", args[0], args[1])); break; }
            }
            else { System.out.println("Неверно указано имя файла или имя пользователя! Повторите ввод\n"); continue; }
        }
    }

    private static void showExitScreen() {
        System.out.println("Как вы хотите выйти?");
        System.out.println();
        System.out.println("[ 1 ] Выйти из сеанса пользователя");
        System.out.println("[ 2 ] Выйти из системы и завершить её работу");

        String userCommand = null;
        while (true) {
            System.out.print(String.format("%s > exit > ", currentUser.getName()));

            inputReader = new BufferedReader(new InputStreamReader(System.in));

            try { userCommand = inputReader.readLine(); } 
            catch (IOException exception) { userCommand = null; }

            if (userCommand == null || userCommand.isBlank() || userCommand.isEmpty()) {
                System.out.println();
                System.out.println("Неверный формат команды! Повторите ввод\n");
                try { 
                    inputReader.reset();
                    inputReader.close(); 
                }
                catch (Exception ignored) {}
                continue;
            }

            switch (userCommand) {
                case "1" -> { Terminal.showLoginScreen(); }
                case "2" -> { System.exit(0); }
                default -> { System.out.println("Команда не распознана! Повторите ввод\n"); continue; }
            }
        }
    }
}
