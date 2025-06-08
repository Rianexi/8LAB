package commands;

import utility.ExecutionResponse;

/**@author Rianexi
Базовый класс для всех команд
 */
public abstract class Command {
    private final String name;
    private final String description;

    /**Конструктор для создания команды
     @param name Название команды
     @param description Описание команды
     */
    protected Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     Выполняет команду с заданными аргументами
     @param arguments Аргументы команды
     @return Результат выполнения команды
     */
    public abstract ExecutionResponse apply(String[] arguments);

    /**
     Выполняет команду с заданным объектом CommandWrapper
     @param wrapper Объект команды
     @return Результат выполнения команды
     */
    public ExecutionResponse apply(CommandWrapper wrapper) {
        return new ExecutionResponse(false, "Команда " + name + " не поддерживает выполнение с CommandWrapper");
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
}