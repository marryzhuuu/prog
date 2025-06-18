package server.network;

import share.network.requests.Request;
import share.network.responses.Response;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.SerializationUtils;
import server.Main;

import share.network.responses.UnknownCommandResponse;
import server.commands.CommandHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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

    protected Selector selector;
    private final int BUFFER_SIZE = 65507; // Максимальный размер UDP пакета

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
        try {
            logger.info("Сервер запущен по адресу " + addr);

            while (running) {
                if (selector.select() > 0) {
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        if (key.isReadable()) {
                            readPool.submit(() -> handleRead(key));
                        }
                        keyIterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Ошибка в селекторе: " + e.toString(), e);
        } finally {
            processPool.shutdown();
            sendPool.shutdown();
            close();
        }
    }

    private final Map<SocketAddress, ByteArrayOutputStream> partialData = new ConcurrentHashMap<>();


    private void handleRead(SelectionKey key) {
        DatagramChannel channel = (DatagramChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        SocketAddress clientAddr;
        boolean isLastChunk = false;

        try {
            clientAddr = channel.receive(buffer);
            if (clientAddr == null) return;

            buffer.flip();
            byte[] chunk = new byte[buffer.remaining()];
            buffer.get(chunk);
            isLastChunk = (chunk[chunk.length - 1] == 1);
            chunk = Arrays.copyOf(chunk, chunk.length - 1);

            // Добавляем чанк к существующим данным клиента
            ByteArrayOutputStream dataStream = partialData.computeIfAbsent(
                    clientAddr, k -> new ByteArrayOutputStream()
            );
            dataStream.write(chunk);

            // Если это последний чанк — обрабатываем
            if (isLastChunk) {
                byte[] fullData = dataStream.toByteArray();
                partialData.remove(clientAddr);
                processRequest(fullData, clientAddr);
            }
        } catch (IOException e) {
            logger.error("Ошибка чтения: " + e.getMessage());
        }
    }

    private void processRequest(byte[] dataFromClient, SocketAddress clientAddr) throws IOException {
        try {
            Request request = SerializationUtils.deserialize(dataFromClient);
            processPool.submit(() -> {
                try {
                    Response response = commandHandler.handle(request);
                    if (response == null) {
                        response = new UnknownCommandResponse(request.getName());
                    }

                    byte[] data = SerializationUtils.serialize(response);

                    sendPool.submit(() -> {
                        try {
                            sendData(data, clientAddr);
                        } catch (Exception e) {
                            logger.error("Ошибка отправки ответа: " + e.toString(), e);
                        }
                    });
                } catch (Exception e) {
                    logger.error("Ошибка обработки запроса: " + e.toString(), e);
                }
            });
        } catch (Exception e) {
            logger.error("Ошибка обработки запроса: " + e.toString(), e);
        }
    }
}
