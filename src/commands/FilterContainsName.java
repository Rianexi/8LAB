package commands;

import managers.CollectionManager;
import utility.Console;
import utility.ExecutionResponse;

/**
 * @author Rianexi
 * Команда для вывода элементов, имя которых содержит подстроку
 */
public class FilterContainsName extends Command {
    private final Console console;
    private final CollectionManager collectionManager;
    /**
     * Конструктор команды
     * @param console Консоль для взаимодействия с пользователем
     * @param collectionManager Менеджер коллекции для выполнения команды
     */
    public FilterContainsName(Console console, CollectionManager collectionManager) {
        super("filter_contains_name", "вывести элементы, имя которых содержит подстроку");
        this.console = console;
        this.collectionManager = collectionManager;
    }
    /**
     * Выполняет вывод элементов, имя которых содержит заданную подстроку
     * @param arguments Аргументы команды (подстрока имени)
     * @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        if (arguments.length != 1) return new ExecutionResponse(false, "Требуется один аргумент: name");
        String name = arguments[0];
        StringBuilder sb = new StringBuilder();
        collectionManager.getCollection().stream()
                .filter(w -> w.getName().contains(name))
                .forEach(w -> sb.append(w).append("\n"));
        return new ExecutionResponse(true, sb.length() > 0 ? sb.toString() : "Ничего не найдено");
    }
}