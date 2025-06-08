package commands;

import utility.Console;
import utility.ExecutionResponse;

/**
 * @author Rianexi
 * Команда для завершения программы
 */
public class Exit extends Command {
    private final Console console;
    /**
     * Конструктор команды
     * @param console Консоль для взаимодействия с пользователем
     */
    public Exit(Console console) {
        super("exit", "завершить программу без сохранения");
        this.console = console;
    }
    /**
     * Выполняет завершение программы
     * @param arguments Аргументы команды (не используются)
     * @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        console.println("До встречи!");
        return new ExecutionResponse(true, "");
    }
}