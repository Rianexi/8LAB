package server;

import utility.CommandSerializer;
import utility.ExecutionResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;
import java.util.logging.Level;

/**@author Rianexi
Модуль отправки ответов клиенту
 */
public class ResponseSender {
    private final OutputStream outputStream;
    private static final Logger LOGGER = Logger.getLogger(ResponseSender.class.getName());

    public ResponseSender(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void sendResponse(ExecutionResponse response) throws IOException {
        synchronized (outputStream) {
            byte[] serialized = CommandSerializer.serialize(response);
            outputStream.write(serialized);
            outputStream.flush();
            LOGGER.info("Ответ успешно отправлен");
        }
    }
}