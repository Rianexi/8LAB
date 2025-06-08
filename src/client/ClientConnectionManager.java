package client;

import utility.CommandSerializer;
import utility.ExecutionResponse;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientConnectionManager {
    private final String host;
    private final int port;
    private SocketChannel socketChannel;
    private static final int BUFFER_SIZE = 1024 * 1024;

    public ClientConnectionManager(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        connect();
    }

    private void connect() throws IOException {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(host, port));
            while (!socketChannel.finishConnect()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Подключение прервано", e);
                }
            }
        } catch (IOException e) {
            throw new IOException("Не удалось подключиться к серверу: " + e.getMessage(), e);
        }
    }

    public ExecutionResponse sendCommand(commands.CommandWrapper commandWrapper) throws IOException {
        int retries = 3;
        while (retries > 0) {
            try {
                if (!socketChannel.isConnected() || !socketChannel.isOpen()) {
                    connect();
                }
                ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                byte[] serialized = CommandSerializer.serialize(commandWrapper);
                buffer.put(serialized);
                buffer.flip();
                socketChannel.write(buffer);

                buffer.clear();
                int bytesRead = 0;
                long startTime = System.currentTimeMillis();
                while (bytesRead == 0 && System.currentTimeMillis() - startTime < 5000) {
                    bytesRead = socketChannel.read(buffer);
                    if (bytesRead == -1) {
                        throw new IOException("Сервер закрыл соединение");
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Чтение ответа прервано", e);
                    }
                }
                if (bytesRead == 0) {
                    throw new IOException("Тайм-аут ожидания ответа от сервера");
                }

                buffer.flip();
                byte[] responseBytes = new byte[buffer.remaining()];
                buffer.get(responseBytes);
                return (ExecutionResponse) CommandSerializer.deserialize(responseBytes);
            } catch (IOException e) {
                retries--;
                if (retries == 0) {
                    throw new IOException("Ошибка связи с сервером: " + e.getMessage(), e);
                }
                try {
                    socketChannel.close();
                    connect();
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Отправка прервана", ie);
                }
            } catch (ClassNotFoundException e) {
                throw new IOException("Ошибка десериализации ответа: " + e.getMessage(), e);
            }
        }
        throw new IOException("Не удалось отправить команду после нескольких попыток");
    }

    public void close() throws IOException {
        if (socketChannel != null && socketChannel.isOpen()) {
            socketChannel.close();
        }
    }
}