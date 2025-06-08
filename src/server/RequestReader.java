package server;

import utility.CommandSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import java.util.logging.Level;

/**@author Rianexi
Модуль чтения запросов от клиента
 */
public class RequestReader {
    private final InputStream inputStream;
    private static final Logger LOGGER = Logger.getLogger(RequestReader.class.getName());

    public RequestReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public commands.CommandWrapper readRequest() throws IOException, ClassNotFoundException {
        byte[] buffer = new byte[1024 * 1024];
        int bytesRead = inputStream.read(buffer);
        if (bytesRead == -1) {
            throw new IOException("Клиент закрыл соединение");
        }
        byte[] data = new byte[bytesRead];
        System.arraycopy(buffer, 0, data, 0, bytesRead);
        LOGGER.info("Запрос успешно прочитан");
        return (commands.CommandWrapper) CommandSerializer.deserialize(data);
    }
}