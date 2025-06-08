package commands;

import managers.CommandManager;
import utility.Console;
import utility.ExecutionResponse;

/**
 * @author Rianexi
 * Команда для вывода справки по доступным командам
 */
public class Help extends Command {
    private final Console console;
    private final CommandManager commandManager;
    /**
     * Конструктор команды
     * @param console Консоль для взаимодействия с пользователем
     * @param commandManager Менеджер команд для получения списка команд
     */
    public Help(Console console, CommandManager commandManager) {
        super("help", "вывести справку по доступным командам");
        this.console = console;
        this.commandManager = commandManager;
    }
    /**
     * Выполняет вывод справки по доступным командам
     * @param arguments Аргументы команды (не используются)
     * @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        StringBuilder sb = new StringBuilder("Доступные команды:\n");
        commandManager.getCommands().forEach((name, cmd) ->
                sb.append(String.format("%s: %s%n", name, cmd.getDescription())));
        return new ExecutionResponse(true, sb.toString());
    }
}