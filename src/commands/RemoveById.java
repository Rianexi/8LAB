package commands;

import managers.CollectionManager;
import models.Worker; // Добавленный импорт
import utility.Console;
import utility.ExecutionResponse;

/**
 * @author Rianexi
 * Команда для удаления элемента по ID
 */
public class RemoveById extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды
     * @param console Консоль для взаимодействия с пользователем
     * @param collectionManager Менеджер коллекции для выполнения команды
     */
    public RemoveById(Console console, CollectionManager collectionManager) {
        super("remove_by_id", "удалить элемент по заданному id");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет удаление элемента по заданному ID
     * @param arguments Аргументы команды (ID работника)
     * @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        if (arguments.length != 1) return new ExecutionResponse(false, "Требуется один аргумент: id");
        try {
            int id = Integer.parseInt(arguments[0]);
            if (collectionManager.getById(id) == null) return new ExecutionResponse(false, "Работник с id " + id + " не найден");
            return new ExecutionResponse(false, "Команда remove_by_id должна вызываться с указанием пользователя");
        } catch (NumberFormatException e) {
            return new ExecutionResponse(false, "id должен быть числом");
        }
    }

    /**
     * Выполняет удаление элемента по заданному ID
     * @param wrapper Объект команды с Worker и User
     * @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(CommandWrapper wrapper) {
        if (wrapper.getArguments().length != 1) {
            return new ExecutionResponse(false, "Требуется один аргумент: id");
        }
        try {
            int id = Integer.parseInt(wrapper.getArguments()[0]);
            Worker worker = collectionManager.getById(id); // Строка 55, теперь должна работать
            if (worker == null) {
                return new ExecutionResponse(false, "Работник с id " + id + " не найден");
            }
            if (!worker.getOwner().getLogin().equals(wrapper.getUser().getLogin())) {
                return new ExecutionResponse(false, "Вы не можете удалить работника, созданного другим пользователем");
            }
            if (collectionManager.removeById(id, wrapper.getUser())) {
                return new ExecutionResponse(true, "Работник с id " + id + " удален!");
            }
            return new ExecutionResponse(false, "Ошибка удаления работника");
        } catch (NumberFormatException e) {
            return new ExecutionResponse(false, "id должен быть числом");
        }
    }
}