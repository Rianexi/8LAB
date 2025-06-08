package server;

import managers.CollectionManager;
import managers.UserManager;
import utility.Console;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;
import java.util.logging.Level;

/**@author Rianexi
Модуль приема подключений
 */
public class ConnectionManager {
    private final int port;
    private final CollectionManager collectionManager;
    private final UserManager userManager;
    private final Console console;
    private static final Logger LOGGER = Logger.getLogger(ConnectionManager.class.getName());
    private final ExecutorService readPool = Executors.newFixedThreadPool(10); // Пул для чтения запросов
    private final ExecutorService processPool = Executors.newFixedThreadPool(10); // Пул для обработки запросов
    private final ForkJoinPool sendPool = ForkJoinPool.commonPool(); // Пул для отправки ответов

    public ConnectionManager(int port, CollectionManager collectionManager, UserManager userManager, Console console) {
        this.port = port;
        this.collectionManager = collectionManager;
        this.userManager = userManager;
        this.console = console;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            LOGGER.info("Сервер ожидает подключений на порту " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                LOGGER.info("Новое подключение: " + clientSocket.getInetAddress());
                readPool.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Ошибка сервера: " + e.getMessage(), e);
            throw e;
        } finally {
            readPool.shutdown();
            processPool.shutdown();
            sendPool.shutdown();
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            RequestReader requestReader = new RequestReader(clientSocket.getInputStream());
            CommandProcessor commandProcessor = new CommandProcessor(collectionManager, userManager, console);
            ResponseSender responseSender = new ResponseSender(clientSocket.getOutputStream());

            while (true) {
                try {
                    commands.CommandWrapper request = requestReader.readRequest();
                    LOGGER.info("Получен запрос: " + request.getCommandName());
                    processPool.submit(() -> {
                        utility.ExecutionResponse response = commandProcessor.process(request);
                        sendPool.submit(() -> {
                            try {
                                responseSender.sendResponse(response);
                                LOGGER.info("Ответ отправлен клиенту");
                            } catch (IOException e) {
                                LOGGER.log(Level.WARNING, "Ошибка отправки ответа: " + e.getMessage(), e);
                            }
                        });
                    });
                    if (request.getCommandName().equals("exit")) {
                        LOGGER.info("Клиент отправил команду exit, закрываем соединение");
                        break;
                    }
                } catch (IOException | ClassNotFoundException e) {
                    LOGGER.log(Level.WARNING, "Ошибка обработки запроса или клиент отключился: " + e.getMessage(), e);
                    break;
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Неожиданная ошибка при обработке клиента: " + e.getMessage(), e);
        } finally {
            try {
                clientSocket.close();
                LOGGER.info("Соединение с клиентом закрыто");
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Ошибка при закрытии соединения: " + e.getMessage(), e);
            }
        }
    }
}