package commands;

import managers.CollectionManager;
import models.Worker;
import utility.Console;
import utility.ExecutionResponse;

/**@author Rianexi
Команда для добавления нового работника
 */
public class Add extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    /**Конструктор команды
     @param console Консоль для взаимодействия с пользователем
     @param collectionManager Менеджер коллекции для выполнения команды
     */
    public Add(Console console, CollectionManager collectionManager) {
        super("add", "добавить новый элемент в коллекцию");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     Выполняет добавление нового работника в коллекцию
     @param arguments Аргументы команды
     @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        return new ExecutionResponse(false, "Команда add должна вызываться через клиент с объектом Worker");
    }

    /**
     Выполняет добавление нового работника из CommandWrapper
     @param wrapper Объект команды с Worker
     @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(CommandWrapper wrapper) {
        if (wrapper.getWorker() == null) {
            return new ExecutionResponse(false, "Для команды add требуется объект Worker");
        }
        Worker worker = wrapper.getWorker();
        if (collectionManager.add(worker, wrapper.getUser())) {
            return new ExecutionResponse(true, "Работник добавлен! ID: " + worker.getId());
        }
        return new ExecutionResponse(false, "Ошибка добавления работника");
    }
}