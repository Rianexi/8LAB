package client;

import utility.Console;
import utility.StandardConsole;
import java.io.IOException;

/**@author Rianexi
Главный класс клиентского приложения
 */
public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5555;

    public static void main(String[] args) {
        try {
            Console console = new StandardConsole();
            ClientConnectionManager connectionManager = new ClientConnectionManager(SERVER_HOST, SERVER_PORT);
            CommandReader commandReader = new CommandReader(console, connectionManager);
            commandReader.start();
        } catch (IOException e) {
            System.err.println("\u001B[31mНе удалось запустить клиент: " + e.getMessage() + "\u001B[0m");
            System.exit(1);
        }
    }
}