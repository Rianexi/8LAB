package managers;

import commands.*;
import models.User;
import utility.Console;
import utility.ExecutionResponse;

import java.util.HashMap;
import java.util.Map;

/**@author Rianexi
Менеджер команд для обработки пользовательского ввода
 */
public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();
    private final Console console;
    private User currentUser;

    /**Конструктор для инициализации менеджера команд
     @param collectionManager Менеджер коллекции для выполнения команд
     @param console Консоль для взаимодействия с пользователем
     @param userManager Менеджер пользователей
     */
    public CommandManager(CollectionManager collectionManager, Console console, UserManager userManager) {
        this.console = console;
        commands.put("help", new Help(console, this));
        commands.put("info", new Info(console, collectionManager));
        commands.put("show", new Show(console, collectionManager));
        commands.put("add", new Add(console, collectionManager));
        commands.put("update", new Update(console, collectionManager));
        commands.put("remove_by_id", new RemoveById(console, collectionManager));
        commands.put("clear", new Clear(console, collectionManager));
        commands.put("execute_script", new ExecuteScript(console, this));
        commands.put("exit", new Exit(console));
        commands.put("insert_at", new InsertAt(console, collectionManager));
        commands.put("add_if_max", new AddIfMax(console, collectionManager));
        commands.put("reorder", new Reorder(console, collectionManager));
        commands.put("remove_all_by_salary", new RemoveAllBySalary(console, collectionManager));
        commands.put("filter_contains_name", new FilterContainsName(console, collectionManager));
        commands.put("filter_less_than_person", new FilterLessThanPerson(console, collectionManager));
        commands.put("login", new Login(console, userManager));
        commands.put("register", new Register(console, userManager));
    }

    /**Выполняет команду по строковому вводу пользователя
     @param commandLine Введенная пользователем строка команды
     @param user Пользователь, выполняющий команду
     @return Результат выполнения команды
     */
    public ExecutionResponse execute(String commandLine, User user) {
        String[] parts = commandLine.trim().split("\\s+", 2);
        String commandName = parts[0];
        String[] args = parts.length > 1 ? parts[1].split("\\s+") : new String[0];

        if (!commandName.equals("login") && !commandName.equals("register") && user == null) {
            return new ExecutionResponse(false, "Требуется авторизация для выполнения команды");
        }
        this.currentUser = user;

        Command command = commands.get(commandName);
        if (command == null) return new ExecutionResponse(false, "Команда '" + commandName + "' не найдена");
        return command.apply(args);
    }

    /**Возвращает текущего пользователя
     @return Текущий пользователь
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**Возвращает карту доступных команд
     @return Карта команд
     */
    public Map<String, Command> getCommands() { return commands; }
}