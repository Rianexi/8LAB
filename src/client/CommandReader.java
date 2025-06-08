package client;

import commands.CommandWrapper;
import models.User;
import models.Worker;
import utility.Console;
import utility.ExecutionResponse;
import utility.PasswordHasher;

import java.io.IOException;

/**@author Rianexi
Модуль чтения и отправки команд
 */
public class CommandReader {
    private final Console console;
    private final ClientConnectionManager connectionManager;
    private User currentUser;

    public CommandReader(Console console, ClientConnectionManager connectionManager) {
        this.console = console;
        this.connectionManager = connectionManager;
    }

    public void start() {
        console.println("\u001B[32mКлиент запущен. Введите 'login <username> <password>' или 'register <username> <password>' для начала работы.\u001B[0m");
        while (true) {
            try {
                String input = console.readln();
                if (input == null || input.trim().isEmpty()) continue;

                String[] parts = input.trim().split("\\s+", 2);
                String commandName = parts[0];
                String[] args = parts.length > 1 ? parts[1].split("\\s+") : new String[0];

                if (commandName.equals("login")) {
                    if (args.length != 2) {
                        console.println("\u001B[31mОшибка: login требует два аргумента (username password)\u001B[0m");
                        continue;
                    }
                    String login = args[0];
                    String password = args[1];
                    CommandWrapper wrapper = new CommandWrapper("login", args, null, null);
                    ExecutionResponse response = connectionManager.sendCommand(wrapper);
                    if (response.isSuccess()) {
                        String[] responseParts = response.getMessage().split("ID: ");
                        int userId = Integer.parseInt(responseParts[1]);
                        currentUser = new User(userId, login, PasswordHasher.hashPassword(password));
                        console.println("\u001B[32m" + response.getMessage() + "\u001B[0m");
                    } else {
                        console.println("\u001B[31m" + response.getMessage() + "\u001B[0m");
                    }
                    continue;
                }

                if (commandName.equals("register")) {
                    if (args.length != 2) {
                        console.println("\u001B[31mОшибка: register требует два аргумента (username password)\u001B[0m");
                        continue;
                    }
                    String login = args[0];
                    String password = args[1];
                    CommandWrapper wrapper = new CommandWrapper("register", args, null, null);
                    ExecutionResponse response = connectionManager.sendCommand(wrapper);
                    if (response.isSuccess()) {
                        String[] responseParts = response.getMessage().split("ID: ");
                        int userId = Integer.parseInt(responseParts[1]);
                        currentUser = new User(userId, login, PasswordHasher.hashPassword(password));
                        console.println("\u001B[32m" + response.getMessage() + "\u001B[0m");
                    } else {
                        console.println("\u001B[31m" + response.getMessage() + "\u001B[0m");
                    }
                    continue;
                }

                if (currentUser == null) {
                    console.println("\u001B[31mОшибка: необходимо авторизоваться с помощью команды login\u001B[0m");
                    continue;
                }

                Worker worker = null;
                if (commandName.equals("add") || commandName.equals("add_if_max")) {
                    console.println("Введите данные для работника:");
                    worker = console.readWorker(0, currentUser);
                } else if (commandName.equals("insert_at")) {
                    if (args.length != 1) {
                        console.println("\u001B[31mОшибка: insert_at требует один аргумент (index)\u001B[0m");
                        continue;
                    }
                    try {
                        int index = Integer.parseInt(args[0]);
                        if (index < 0) {
                            console.println("\u001B[31mОшибка: index должен быть неотрицательным\u001B[0m");
                            continue;
                        }
                        console.println("Введите данные для работника:");
                        worker = console.readWorker(0, currentUser);
                    } catch (NumberFormatException e) {
                        console.println("\u001B[31mОшибка: index должен быть числом\u001B[0m");
                        continue;
                    }
                } else if (commandName.equals("update")) {
                    if (args.length != 1) {
                        console.println("\u001B[31mОшибка: update требует один аргумент (id)\u001B[0m");
                        continue;
                    }
                    try {
                        int id = Integer.parseInt(args[0]);
                        if (id <= 0) {
                            console.println("\u001B[31mОшибка: id должен быть положительным\u001B[0m");
                            continue;
                        }
                        console.println("Введите новые данные для работника:");
                        worker = console.readWorker(id, currentUser);
                    } catch (NumberFormatException e) {
                        console.println("\u001B[31mОшибка: id должен быть числом\u001B[0m");
                        continue;
                    }
                }

                CommandWrapper wrapper = new CommandWrapper(commandName, args, worker, currentUser);
                ExecutionResponse response = connectionManager.sendCommand(wrapper);
                console.println(response.getMessage());
            } catch (IllegalArgumentException e) {
                console.println("\u001B[31mОшибка: " + e.getMessage() + "\u001B[0m");
            } catch (IOException e) {
                console.println("\u001B[31mСервер недоступен: " + e.getMessage() + ". Попробуйте снова.\u001B[0m");
            }
        }
    }
}