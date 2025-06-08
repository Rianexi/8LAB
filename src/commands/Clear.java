package commands;

import managers.CollectionManager;
import utility.Console;
import utility.ExecutionResponse;

/**
 * @author Rianexi
 * Команда для очистки коллекции
 */
public class Clear extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды
     * @param console Консоль для взаимодействия с пользователем
     * @param collectionManager Менеджер коллекции для выполнения команды
     */
    public Clear(Console console, CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет очистку коллекции
     * @param arguments Аргументы команды (не используются)
     * @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        return new ExecutionResponse(false, "Команда clear должна вызываться с указанием пользователя");
    }

    /**
     * Выполняет очистку коллекции для пользователя
     * @param wrapper Объект команды с User
     * @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(CommandWrapper wrapper) {
        if (wrapper.getUser() == null) {
            return new ExecutionResponse(false, "Требуется авторизация для выполнения команды");
        }
        int removed = collectionManager.clear(wrapper.getUser());
        return new ExecutionResponse(true, "Удалено элементов: " + removed);
    }
}