package server;

import managers.CollectionManager;
import managers.DatabaseManager;
import managers.UserManager;
import utility.Console;
import utility.StandardConsole;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

/**@author Rianexi
Главный класс серверного приложения
 */
public class Server {
    private static final int PORT = 5555;
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) {
        try {
            LOGGER.info("Сервер запускается...");
            Console console = new StandardConsole();
            DatabaseManager databaseManager = new DatabaseManager();
            UserManager userManager = new UserManager(databaseManager);
            CollectionManager collectionManager = new CollectionManager(databaseManager);
            ConnectionManager connectionManager = new ConnectionManager(PORT, collectionManager, userManager, console);
            connectionManager.start();
            LOGGER.info("Сервер успешно запущен на порту " + PORT);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Не удалось запустить сервер: " + e.getMessage(), e);
            System.exit(1);
        }
    }
}