package server;

import commands.Command;
import commands.CommandWrapper;
import managers.CollectionManager;
import managers.CommandManager;
import managers.UserManager;
import utility.Console;
import utility.ExecutionResponse;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.logging.Level;

/**@author Rianexi
Модуль обработки команд
 */
public class CommandProcessor {
    private final CollectionManager collectionManager;
    private final UserManager userManager;
    private final CommandManager commandManager;
    private static final Logger LOGGER = Logger.getLogger(CommandProcessor.class.getName());

    public CommandProcessor(CollectionManager collectionManager, UserManager userManager, Console console) {
        this.collectionManager = collectionManager;
        this.userManager = userManager;
        this.commandManager = new CommandManager(collectionManager, console, userManager);
    }

    public ExecutionResponse process(CommandWrapper wrapper) {
        try {
            LOGGER.info("Обработка команды: " + wrapper.getCommandName());
            String commandName = wrapper.getCommandName();

            // Проверка авторизации
            if (!commandName.equals("login") && !commandName.equals("register")) {
                if (wrapper.getUser() == null) {
                    return new ExecutionResponse(false, "Требуется авторизация для выполнения команды");
                }
            }

            // Для команд, требующих Worker
            if (commandName.equals("add") || commandName.equals("add_if_max") ||
                    commandName.equals("insert_at") || commandName.equals("update") ||
                    commandName.equals("remove_by_id") || commandName.equals("clear") ||
                    commandName.equals("remove_all_by_salary")) {
                Command command = commandManager.getCommands().get(commandName);
                if (command != null) {
                    // Убедимся, что у Worker установлен правильный владелец
                    if (wrapper.getWorker() != null && wrapper.getUser() != null) {
                        wrapper.getWorker().setOwner(wrapper.getUser());
                    }
                    return command.apply(wrapper);
                }
                return new ExecutionResponse(false, "Команда " + commandName + " не найдена");
            }

            // Для остальных команд
            ExecutionResponse response = commandManager.execute(
                    wrapper.getCommandName() + " " + String.join(" ", wrapper.getArguments()),
                    wrapper.getUser());
            if (response.isSuccess() && wrapper.getCommandName().equals("show")) {
                LinkedList<models.Worker> sorted = new LinkedList<>(collectionManager.getCollection());
                sorted.sort(Comparator.comparing(models.Worker::getName));
                StringBuilder sb = new StringBuilder();
                sorted.forEach(worker -> sb.append(worker).append("\n"));
                return new ExecutionResponse(true, sb.length() > 0 ? sb.toString() : "Коллекция пуста!");
            }
            return response;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Ошибка обработки команды: " + e.getMessage(), e);
            return new ExecutionResponse(false, "Ошибка: " + e.getMessage());
        }
    }
}