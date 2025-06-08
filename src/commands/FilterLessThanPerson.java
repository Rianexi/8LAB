package commands;

import managers.CollectionManager;
import models.User;
import models.Worker;
import utility.Console;
import utility.ExecutionResponse;

/**@author Rianexi
Команда для вывода элементов, у которых поле person меньше указанного
 */
public class FilterLessThanPerson extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    /**Конструктор команды
     @param console Консоль для взаимодействия с пользователем
     @param collectionManager Менеджер коллекции
     */
    public FilterLessThanPerson(Console console, CollectionManager collectionManager) {
        super("filter_less_than_person", "вывести элементы, значение поля person которых меньше заданного");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**Выполняет команду
     @param arguments Аргументы команды (не используются)
     @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        return new ExecutionResponse(false, "Команда filter_less_than_person должна вызываться через CommandWrapper");
    }

    /**Выполняет команду с использованием CommandWrapper
     @param wrapper Объект команды с Worker и User
     @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(CommandWrapper wrapper) {
        try {
            console.println("Введите данные для person, с которым будет производиться сравнение:");
            // Исправленная строка 33: добавлен wrapper.getUser()
            Worker worker = console.readWorker(0, wrapper.getUser());
            if (worker.getPerson() == null) {
                return new ExecutionResponse(false, "Person не задан, сравнение невозможно");
            }

            StringBuilder result = new StringBuilder();
            synchronized (collectionManager.getCollection()) {
                for (Worker w : collectionManager.getCollection()) {
                    if (w.getPerson() != null && w.getPerson().compareTo(worker.getPerson()) < 0) {
                        result.append(w.toString()).append("\n");
                    }
                }
            }

            if (result.length() == 0) {
                return new ExecutionResponse(true, "Нет элементов с person меньше указанного");
            }
            return new ExecutionResponse(true, result.toString().trim());
        } catch (IllegalArgumentException e) {
            return new ExecutionResponse(false, "Ошибка ввода данных: " + e.getMessage());
        }
    }
}