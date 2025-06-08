package commands;

import managers.CollectionManager;
import utility.Console;
import utility.ExecutionResponse;

import java.util.Collections;

/**
 * @author Rianexi
 * Команда для сортировки коллекции в обратном порядке
 */
public class Reorder extends Command {
    private final Console console;
    private final CollectionManager collectionManager;
    /**
     * Конструктор команды
     * @param console Консоль для взаимодействия с пользователем
     * @param collectionManager Менеджер коллекции для выполнения команды
     */
    public Reorder(Console console, CollectionManager collectionManager) {
        super("reorder", "отсортировать коллекцию в обратном порядке");
        this.console = console;
        this.collectionManager = collectionManager;
    }
    /**
     * Выполняет сортировку коллекции в обратном порядке
     * @param arguments Аргументы команды (не используются)
     * @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        Collections.reverse(collectionManager.getCollection());
        return new ExecutionResponse(true, "Коллекция перевернута!");
    }
}