package utility;

import java.io.Serializable;

/**@author Rianexi
Результат выполнения команды
 */
public class ExecutionResponse implements Serializable {
    private final boolean success;
    private final String message;

    /**Конструктор для создания результата выполнения команды
     @param success Успешность выполнения команды
     @param message Сообщение с результатом выполнения
     */
    public ExecutionResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**Проверяет, успешно ли выполнена команда
     @return true, если команда выполнена успешно, иначе false
     */
    public boolean isSuccess() { return success; }

    /**Возвращает сообщение результата выполнения команды
     @return Сообщение результата
     */
    public String getMessage() { return message; }
}