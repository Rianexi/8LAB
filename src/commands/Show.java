package commands;

import managers.CollectionManager;
import utility.Console;
import utility.ExecutionResponse;

/**
 * @author Rianexi
 * Команда для вывода всех элементов коллекции
 */
public class Show extends Command {
    private final Console console;
    private final CollectionManager collectionManager;
    /**
     * Конструктор команды
     * @param console Консоль для взаимодействия с пользователем
     * @param collectionManager Менеджер коллекции для выполнения команды
     */
    public Show(Console console, CollectionManager collectionManager) {
        super("show", "вывести все элементы коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет вывод всех элементов коллекции
     * @param arguments Аргументы команды (не используются)
     * @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        if (collectionManager.getCollection().isEmpty()) {
            return new ExecutionResponse(true, "Коллекция пуста!");
        }
        StringBuilder sb = new StringBuilder();
        collectionManager.getCollection().forEach(worker -> sb.append(worker).append("\n"));
        return new ExecutionResponse(true, sb.toString());
    }
}