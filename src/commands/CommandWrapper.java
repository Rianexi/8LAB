package commands;

import models.User;
import models.Worker;
import java.io.Serializable;

/**@author Rianexi
Класс-обертка для команд и их аргументов
 */
public class CommandWrapper implements Serializable {
    private final String commandName;
    private final String[] arguments;
    private final Worker worker;
    private final User user;

    public CommandWrapper(String commandName, String[] arguments, Worker worker, User user) {
        if (commandName == null || commandName.trim().isEmpty()) {
            throw new IllegalArgumentException("Название команды не может быть пустым");
        }
        this.commandName = commandName;
        this.arguments = arguments != null ? arguments : new String[0];
        this.worker = worker;
        this.user = user;
    }

    public String getCommandName() { return commandName; }
    public String[] getArguments() { return arguments; }
    public Worker getWorker() { return worker; }
    public User getUser() { return user; }
}