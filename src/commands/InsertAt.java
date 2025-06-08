package commands;

import managers.CollectionManager;
import models.Worker;
import utility.Console;
import utility.ExecutionResponse;

/**@author Rianexi
Команда для вставки элемента на заданную позицию
 */
public class InsertAt extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    /**Конструктор команды
     @param console Консоль для взаимодействия с пользователем
     @param collectionManager Менеджер коллекции для выполнения команды
     */
    public InsertAt(Console console, CollectionManager collectionManager) {
        super("insert_at", "добавить элемент на заданную позицию");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     Выполняет вставку элемента на заданную позицию
     @param arguments Аргументы команды
     @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        return new ExecutionResponse(false, "Команда insert_at должна вызываться через клиент с объектом Worker");
    }

    /**
     Выполняет вставку элемента на заданную позицию
     @param wrapper Объект команды с Worker
     @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(CommandWrapper wrapper) {
        if (wrapper.getArguments().length != 1) {
            return new ExecutionResponse(false, "Требуется один аргумент: index");
        }
        if (wrapper.getWorker() == null) {
            return new ExecutionResponse(false, "Для команды insert_at требуется объект Worker");
        }
        try {
            int index = Integer.parseInt(wrapper.getArguments()[0]);
            if (index < 0 || index > collectionManager.getCollection().size()) {
                return new ExecutionResponse(false, "Индекс вне диапазона");
            }
            Worker worker = wrapper.getWorker();
            if (collectionManager.add(worker, wrapper.getUser())) {
                synchronized (collectionManager.getCollection()) {
                    collectionManager.getCollection().add(index, worker);
                }
                return new ExecutionResponse(true, "Работник добавлен на позицию " + index + "! ID: " + worker.getId());
            }
            return new ExecutionResponse(false, "Ошибка добавления работника");
        } catch (NumberFormatException e) {
            return new ExecutionResponse(false, "index должен быть числом");
        }
    }
}