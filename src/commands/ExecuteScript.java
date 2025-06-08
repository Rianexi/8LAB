package commands;

import managers.CommandManager;
import utility.Console;
import utility.ExecutionResponse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**@author Rianexi
Команда для выполнения скрипта из файла
 */
public class ExecuteScript extends Command {
    private final Console console;
    private final CommandManager commandManager;
    private final Set<String> executedScripts = new HashSet<>();

    /**Конструктор команды
     @param console Консоль для взаимодействия с пользователем
     @param commandManager Менеджер команд для выполнения команд из скрипта
     */
    public ExecuteScript(Console console, CommandManager commandManager) {
        super("execute_script", "выполнить скрипт из файла");
        this.console = console;
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команды из указанного файла скрипта
     * @param arguments Аргументы команды (путь к файлу)
     * @return Результат выполнения команды
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        if (arguments.length != 1) {
            return new ExecutionResponse(false, "Требуется один аргумент: путь к файлу");
        }
        String filePath = arguments[0];

        if (executedScripts.contains(filePath)) {
            return new ExecutionResponse(false, "Скрипт " + filePath + " уже выполняется (рекурсия запрещена)");
        }

        executedScripts.add(filePath);
        List<String> output = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Исправленная строка: передаем текущего пользователя
                ExecutionResponse response = commandManager.execute(line, commandManager.getCurrentUser());
                output.add(response.getMessage());

                if (!response.isSuccess()) {
                    output.add("Прервано из-за ошибки в команде: " + line);
                    break;
                }
            }
            executedScripts.remove(filePath);
            return new ExecutionResponse(true, String.join("\n", output));
        } catch (IOException e) {
            executedScripts.remove(filePath);
            return new ExecutionResponse(false, "Ошибка чтения файла: " + e.getMessage());
        }
    }
}