package commands;

import managers.CollectionManager;
import utility.Console;
import utility.ExecutionResponse;

/**
 * @author Rianexi
 * Команда для вывода информации о коллекции
 */
public class Info extends Command {
    private final Console console;
    private final CollectionManager collectionManager;
    /**
     * Конструктор команды
     * @param console Консоль для взаимодействия с пользователем
     * @param collectionManager Менеджер коллекции для выполнения команды
     */
    public Info(Console console, CollectionManager collectionManager) {
        super("info", "вывести информацию о коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет вывод информации о коллекции
     * @param arguments Аргументы команды (не используются)
     * @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        String info = String.format("Тип: LinkedList, Дата инициализации: %s, Количество элементов: %d",
                collectionManager.getInitializationDate(), collectionManager.getCollection().size());
        return new ExecutionResponse(true, info);
    }
}