package client;

import utility.Console;
import utility.ExecutionResponse;

/**@author Rianexi
Модуль обработки ответов от сервера
 */
public class ResponseHandler {
    private final Console console;

    public ResponseHandler(Console console) {
        this.console = console;
    }

    public void handle(ExecutionResponse response) {
        console.println(response.getMessage());
    }
}