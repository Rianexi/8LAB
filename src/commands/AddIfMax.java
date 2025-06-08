package commands;

import managers.CollectionManager;
import models.Worker;
import utility.Console;
import utility.ExecutionResponse;

/**@author Rianexi
Команда для добавления элемента, если он больше максимального
 */
public class AddIfMax extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    /**Конструктор команды
     @param console Консоль для взаимодействия с пользователем
     @param collectionManager Менеджер коллекции для выполнения команды
     */
    public AddIfMax(Console console, CollectionManager collectionManager) {
        super("add_if_max", "добавить элемент, если он больше максимального");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     Выполняет добавление элемента, если он больше максимального
     @param arguments Аргументы команды
     @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        return new ExecutionResponse(false, "Команда add_if_max должна вызываться через клиент с объектом Worker");
    }

    /**
     Выполняет добавление элемента, если он больше максимального
     @param wrapper Объект команды с Worker
     @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(CommandWrapper wrapper) {
        if (wrapper.getWorker() == null) {
            return new ExecutionResponse(false, "Для команды add_if_max требуется объект Worker");
        }
        Worker worker = wrapper.getWorker();
        synchronized (collectionManager.getCollection()) {
            if (collectionManager.getCollection().isEmpty() ||
                    worker.compareTo(collectionManager.getCollection().stream().max(Worker::compareTo).get()) > 0) {
                if (collectionManager.add(worker, wrapper.getUser())) {
                    return new ExecutionResponse(true, "Работник добавлен как максимальный! ID: " + worker.getId());
                }
                return new ExecutionResponse(false, "Ошибка добавления работника");
            }
            return new ExecutionResponse(false, "Работник не больше максимального, добавление отменено");
        }
    }
}