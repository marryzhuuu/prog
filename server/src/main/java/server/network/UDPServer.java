package server.network;

import share.network.requests.Request;
import share.network.responses.Response;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.SerializationUtils;
import server.Main;

import share.network.responses.UnknownCommandResponse;
import server.commands.CommandHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Абстрактный класс UDP сервера
 */
abstract class UDPServer {
    private final InetSocketAddress addr;
    private final CommandHandler commandHandler;
    // ExecutorService для многопоточного чтения запросов
    private final ExecutorService readPool = Executors.newCachedThreadPool(new LoggingThreadFactory());
    // ExecutorService для многопоточной обработки запросов
    private final ExecutorService processPool = Executors.newFixedThreadPool(10, new LoggingThreadFactory()); // Например, 10 потоков
    // ExecutorService для многопоточной отправки ответов
    private final ExecutorService sendPool = Executors.newCachedThreadPool(new LoggingThreadFactory());

    private final Logger logger = Main.logger;

    private boolean running = true;

    public UDPServer(InetSocketAddress addr, CommandHandler commandHandler) {
        this.addr = addr;
        this.commandHandler = commandHandler;
    }

    public InetSocketAddress getAddr() {
        return addr;
    }

    /**
     * Получает данные с клиента.
     * Возвращает пару из данных и адреса клиента
     */
    public abstract Pair<Byte[], SocketAddress> receiveData() throws IOException;

    /**
     * Отправляет данные клиенту
     */
    public abstract void sendData(byte[] data, SocketAddress addr) throws IOException;

    public abstract void connectToClient(SocketAddress addr) throws SocketException;

    public abstract void disconnectFromClient();
    public abstract void close();

    public static class LoggingThreadFactory implements ThreadFactory {
        private static final Logger logger = LogManager.getLogger(LoggingThreadFactory.class.getName());

        @Override
        public Thread newThread(Runnable r) {
            // Создаем новый поток с пользовательским именем
            Thread thread = new Thread(r) {
                @Override
                public void run() {
                    logger.info("Thread " + getName() + " started.");
                    try {
                        super.run();
                    } finally {
                        logger.info("Thread " + getName() + " finished.");
                    }
                }
            };

            // Устанавливаем имя потока для удобства логирования
            thread.setName("CachedThreadPool-" + thread.getId());
            return thread;
        }
    }

    public void run() {
        logger.info("Сервер запущен по адресу " + addr);

        while (running) {
            Pair<Byte[], SocketAddress> dataPair;
            logger.info("1 - ожидание данных");
            try {
                dataPair = receiveData();
            } catch (Exception e) {
                logger.error("Ошибка получения данных : " + e.toString(), e);
                disconnectFromClient();
                continue;
            }
            var dataFromClient = dataPair.getKey();
            var clientAddr = dataPair.getValue();
            logger.info("2 - получены данные: " + Arrays.toString(dataFromClient) + clientAddr);

            processRequest(dataFromClient, clientAddr);
        }
        readPool.shutdown();
        processPool.shutdown();
        sendPool.shutdown();
        close();
    }

    private void processRequest(Byte[] dataFromClient, SocketAddress clientAddr) {
        try {
            connectToClient(clientAddr);
            logger.info("3 - установлено соединение с клиентом, десериализация данных из запроса...");
            Request request = SerializationUtils.deserialize(ArrayUtils.toPrimitive(dataFromClient));
            logger.info("4 - обработка запроса: " + request + "...");
            processPool.submit(() -> {
                logger.info("4 - обработка запроса в отдельном потоке: " + request);
                try {
                    Response response = commandHandler.handle(request);
                    if (response == null) {
                        response = new UnknownCommandResponse(request.getName());
                    }

                    byte[] data = SerializationUtils.serialize(response);

                    sendPool.submit(() -> {
                    logger.info("5 - отправка ответа в отдельном потоке: " + Arrays.toString(data));
                        try {
                            sendData(data, clientAddr);
                        } catch (Exception e) {
                            logger.error("Ошибка отправки ответа: " + e.toString(), e);
                        } finally {
                            disconnectFromClient();
                        }
                    });
                } catch (Exception e) {
                    logger.error("Ошибка обработки запроса: " + e.toString(), e);
                }
            });
        } catch (Exception e) {
            logger.error("Ошибка обработки запроса: " + e.toString(), e);
            disconnectFromClient();
        }
    }

//    private void handleRead(SelectionKey key) {
//        DatagramChannel channel = (DatagramChannel) key.channel();
//        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
//        SocketAddress clientAddr;
//
//        try {
//            clientAddr = channel.receive(buffer);
//            if (clientAddr != null) {
//                buffer.flip();
//                byte[] data = new byte[buffer.remaining()];
//                buffer.get(data);
//
//                logger.info("Получены данные от " + clientAddr);
//                processRequest(ArrayUtils.toObject(data), clientAddr);
//            }
//        } catch (IOException e) {
//            logger.error("Ошибка чтения данных: " + e.toString(), e);
//        }
//    }
}
