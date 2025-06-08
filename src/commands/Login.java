package commands;

import managers.UserManager;
import models.User;
import utility.Console;
import utility.ExecutionResponse;

/**@author Rianexi
Команда для авторизации пользователя
 */
public class Login extends Command {
    private final Console console;
    private final UserManager userManager;

    public Login(Console console, UserManager userManager) {
        super("login", "авторизовать пользователя");
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
        User user = userManager.loginUser(login, password);
        if (user != null) {
            return new ExecutionResponse(true, "Пользователь " + login + " успешно авторизован. ID: " + user.getId());
        }
        return new ExecutionResponse(false, "Неверный логин или пароль");
    }
}