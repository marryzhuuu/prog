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

            readPool.submit(() -> {
                logger.info("2 - получены данные в отдельном потоке: "  + Arrays.toString(dataFromClient) + clientAddr);
                try {
                    connectToClient(clientAddr);
                    logger.info("Соединено с " + clientAddr);
                } catch (Exception e) {
                    logger.error("Ошибка соединения с клиентом : " + e.toString(), e);
                }
                logger.info("3 - установлено соединение с клиентом, десериализация данных из запроса...");
                Request request;
                try {
                    request = SerializationUtils.deserialize(ArrayUtils.toPrimitive(dataFromClient));
                    logger.info("Обработка " + request + " из " + clientAddr);
                } catch (SerializationException e) {
                    logger.error("Невозможно десериализовать объект запроса.", e);
                    disconnectFromClient();
                    return;
                }
                logger.info("4 - обработка запроса: " + request + "...");
                processPool.submit(() -> {
                    logger.info("4 - обработка запроса в отдельном потоке: " + request);
                    Response response = null;
                    try {
                        response = commandHandler.handle(request);
                    } catch (Exception e) {
                        logger.error("Ошибка выполнения команды : " + e.toString(), e);
                    }
                    if (response == null) response = new UnknownCommandResponse(request.getName());

                    var data = SerializationUtils.serialize(response);
                    logger.info("Ответ: " + response);

                    sendPool.submit(() -> {
                        logger.info("5 - отправка ответа в отдельном потоке: " + Arrays.toString(data));
                        try {
                            sendData(data, clientAddr);
                            logger.info("Отправлен ответ клиенту " + clientAddr);
                        } catch (Exception e) {
                            logger.error("Ошибка ввода-вывода : " + e.toString(), e);
                        }
                    });
                    disconnectFromClient();
                    logger.info("Отключение от клиента " + clientAddr);
                });
            });
        }
        readPool.shutdown();
        processPool.shutdown();
        sendPool.shutdown();
        close();
    }
}
