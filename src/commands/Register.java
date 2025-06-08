package commands;

import managers.UserManager;
import models.User;
import utility.Console;
import utility.ExecutionResponse;

/**@author Rianexi
Команда для регистрации нового пользователя
 */
public class Register extends Command {
    private final Console console;
    private final UserManager userManager;

    public Register(Console console, UserManager userManager) {
        super("register", "зарегистрировать нового пользователя");
        this.console = console;
        this.userManager = userManager;
    }

    @Override
    public ExecutionResponse apply(String[] arguments) {
        if (arguments.length != 2) {
            return new ExecutionResponse(false, "Требуется два аргумента: login password");
        }
        String login = arguments[0];
        String password = arguments[1];
        User user = userManager.registerUser(login, password);
        if (user != null) {
            return new ExecutionResponse(true, "Пользователь " + login + " успешно зарегистрирован. ID: " + user.getId());
        }
        return new ExecutionResponse(false, "Ошибка регистрации: пользователь уже существует или данные некорректны");
    }
}