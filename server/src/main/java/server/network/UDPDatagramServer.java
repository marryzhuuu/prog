package server.network;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;
import server.Main;
import server.commands.CommandHandler;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public class UDPDatagramServer extends UDPServer {
    private final DatagramChannel datagramChannel;
    private final int BUFFER_SIZE = 65507;
    private final Logger logger = Main.logger;

    public UDPDatagramServer(InetAddress address, int port, CommandHandler commandHandler) throws IOException {
        super(new InetSocketAddress(address, port), commandHandler);
        this.datagramChannel = DatagramChannel.open();
        this.datagramChannel.configureBlocking(false);
        this.datagramChannel.bind(getAddr());
        this.selector = Selector.open();
        this.datagramChannel.register(selector, SelectionKey.OP_READ);
    }

    @Override
    public Pair<Byte[], SocketAddress> receiveData() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        SocketAddress clientAddr = datagramChannel.receive(buffer);
        buffer.flip();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        return new ImmutablePair<>(ArrayUtils.toObject(data), clientAddr);
    }


@Override
public void sendData(byte[] data, SocketAddress addr) throws IOException {
    final int PACKET_SIZE = 1024; // Общий размер пакета
    final int DATA_SIZE = PACKET_SIZE - 1; // Данные + 1 байт маркера

    ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);
    int totalSent = 0;
    int packetsCount = (int) Math.ceil((double) data.length / DATA_SIZE);

    logger.info("Отправляется " + packetsCount + " пакетов...");

    for (int i = 0; i < packetsCount; i++) {
        buffer.clear();

        // Определяем границы текущего чанка
        int start = i * DATA_SIZE;
        int end = Math.min(start + DATA_SIZE, data.length);
        int chunkSize = end - start;

        // Заполняем буфер данными
        buffer.put(data, start, chunkSize);

        // Добавляем маркер конца (1 для последнего пакета, 0 для остальных)
        byte marker = (i == packetsCount - 1) ? (byte)1 : (byte)0;
        buffer.put(marker);

        // Подготавливаем буфер к отправке
        buffer.flip();

        // Отправляем пакет
        while (buffer.hasRemaining()) {
            datagramChannel.send(buffer, addr);
        }

        if (marker == 1) {
            logger.info("Последний пакет размером " + (chunkSize + 1) + " байт отправлен");
        } else {
            logger.info("Пакет " + (i + 1) + "/" + packetsCount + " размером " + (chunkSize + 1) + " байт отправлен");
        }

        totalSent += chunkSize;
    }

    logger.info("Отправка завершена. Всего отправлено " + totalSent + " байт данных");
}

    @Override
    public void close() {
        try {
            if (datagramChannel != null && datagramChannel.isOpen()) {
                datagramChannel.close();
            }
            if (selector != null && selector.isOpen()) {
                selector.close();
            }
        } catch (IOException e) {
            logger.error("Ошибка при закрытии ресурсов", e);
        }
    }
}
