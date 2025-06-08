package commands;

import managers.CollectionManager;
import models.Worker;
import utility.Console;
import utility.ExecutionResponse;

/**@author Rianexi
Команда для обновления элемента по ID
 */
public class Update extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    /**Конструктор команды
     @param console Консоль для взаимодействия с пользователем
     @param collectionManager Менеджер коллекции для выполнения команды
     */
    public Update(Console console, CollectionManager collectionManager) {
        super("update", "обновить элемент по заданному id");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     Выполняет обновление элемента по заданному ID
     @param arguments Аргументы команды
     @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        return new ExecutionResponse(false, "Команда update должна вызываться через клиент с объектом Worker");
    }

    /**
     Выполняет обновление элемента по заданному ID
     @param wrapper Объект команды с Worker
     @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(CommandWrapper wrapper) {
        if (wrapper.getArguments().length != 1) {
            return new ExecutionResponse(false, "Требуется один аргумент: id");
        }
        if (wrapper.getWorker() == null) {
            return new ExecutionResponse(false, "Для команды update требуется объект Worker");
        }
        try {
            int id = Integer.parseInt(wrapper.getArguments()[0]);
            Worker oldWorker = collectionManager.getById(id);
            if (oldWorker == null) {
                return new ExecutionResponse(false, "Работник с id " + id + " не найден");
            }
            if (!oldWorker.getOwner().getLogin().equals(wrapper.getUser().getLogin())) {
                return new ExecutionResponse(false, "Вы не можете обновить работника, созданного другим пользователем");
            }
            Worker newWorker = wrapper.getWorker();
            newWorker.setId(id);
            newWorker.setOwner(wrapper.getUser());
            if (collectionManager.update(newWorker, wrapper.getUser())) {
                return new ExecutionResponse(true, "Работник с id " + id + " обновлен!");
            }
            return new ExecutionResponse(false, "Ошибка обновления работника");
        } catch (NumberFormatException e) {
            return new ExecutionResponse(false, "id должен быть числом");
        }
    }
}