package commands;

import managers.CollectionManager;
import utility.Console;
import utility.ExecutionResponse;

/**
 * @author Rianexi
 * Команда для удаления всех элементов с заданной зарплатой
 */
public class RemoveAllBySalary extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды
     * @param console Консоль для взаимодействия с пользователем
     * @param collectionManager Менеджер коллекции для выполнения команды
     */
    public RemoveAllBySalary(Console console, CollectionManager collectionManager) {
        super("remove_all_by_salary", "удалить все элементы с заданной зарплатой");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет удаление всех элементов с заданной зарплатой
     * @param arguments Аргументы команды (зарплата)
     * @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        if (arguments.length != 1) return new ExecutionResponse(false, "Требуется один аргумент: salary");
        try {
            Integer.parseInt(arguments[0]);
            return new ExecutionResponse(false, "Команда remove_all_by_salary должна вызываться с указанием пользователя");
        } catch (NumberFormatException e) {
            return new ExecutionResponse(false, "salary должен быть числом");
        }
    }

    /**
     * Выполняет удаление всех элементов с заданной зарплатой для пользователя
     * @param wrapper Объект команды с User
     * @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(CommandWrapper wrapper) {
        if (wrapper.getArguments().length != 1) return new ExecutionResponse(false, "Требуется один аргумент: salary");
        try {
            int salary = Integer.parseInt(wrapper.getArguments()[0]);
            int removed = collectionManager.removeBySalary(salary, wrapper.getUser());
            return new ExecutionResponse(true, "Удалено элементов: " + removed);
        } catch (NumberFormatException e) {
            return new ExecutionResponse(false, "salary должен быть числом");
        }
    }
}