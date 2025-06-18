package client.network;

import client.Main;
import share.network.requests.Request;
import share.network.responses.Response;

import com.google.common.primitives.Bytes;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UDPClient {
    private final int PACKET_SIZE = 1024;
    private final int DATA_SIZE = PACKET_SIZE - 1;

    private final DatagramChannel client;
    private final InetSocketAddress addr;
    private final ByteBuffer receiveBuffer;

    private final Logger logger = Main.logger;

    public UDPClient(InetAddress address, int port) throws IOException {
        this.addr = new InetSocketAddress(address, port);
        this.client = DatagramChannel.open();
        this.client.bind(null);
        this.client.connect(addr);
        this.client.configureBlocking(false);
        this.receiveBuffer = ByteBuffer.allocateDirect(PACKET_SIZE); // Используем direct buffer
        logger.info("DatagramChannel подключен к " + addr);
    }

    public Response sendAndReceiveCommand(Request request) throws IOException {
        var data = SerializationUtils.serialize(request);
        var responseBytes = sendAndReceiveData(data);
        Response response = SerializationUtils.deserialize(responseBytes);
        logger.info("Получен ответ от сервера: " + response);
        return response;
    }

    private void sendData(byte[] data) throws IOException {
        byte[][] chunks = splitIntoChunks(data);
        logger.info("Отправляется " + chunks.length + " чанков...");

        for (int i = 0; i < chunks.length; i++) {
            byte[] chunk = markChunk(chunks[i], i == chunks.length - 1);
            sendChunk(chunk);
        }
        logger.info("Отправка данных завершена.");
    }

    private byte[][] splitIntoChunks(byte[] data) {
        byte[][] ret = new byte[(int)Math.ceil(data.length / (double)DATA_SIZE)][];
        int start = 0;
        for(int i = 0; i < ret.length; i++) {
            int end = Math.min(start + DATA_SIZE, data.length);
            ret[i] = Arrays.copyOfRange(data, start, end);
            start += DATA_SIZE;
        }
        return ret;
    }

    private byte[] markChunk(byte[] chunk, boolean isLast) {
        return Bytes.concat(chunk, new byte[]{isLast ? (byte)1 : (byte)0});
    }

    private void sendChunk(byte[] chunk) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(chunk);
        while (buffer.hasRemaining()) {
            client.write(buffer);
        }
        logger.info("Чанк размером " + chunk.length + " отправлен на сервер.");
    }

    private byte[] receiveData() throws IOException {
        List<byte[]> chunks = new ArrayList<>();
        boolean isLastPacket = false;
        int timeoutMs = 5000; // Таймаут 5 секунд
        long startTime = System.currentTimeMillis();

        while (!isLastPacket) {
            if (System.currentTimeMillis() - startTime > timeoutMs) {
                throw new IOException("Timeout while waiting for data");
            }

            byte[] packet = receivePacket();
            if (packet != null) {
                processReceivedPacket(packet, chunks);
                isLastPacket = (packet[packet.length - 1] == 1);
            }
        }

        logger.info("Получено " + chunks.size() + " пакетов");
        return combineChunks(chunks);
    }

    private byte[] receivePacket() throws IOException {
        receiveBuffer.clear();
        int bytesRead = client.read(receiveBuffer);

        if (bytesRead == -1) {
            throw new IOException("Connection closed");
        }

        if (bytesRead > 0) {
            receiveBuffer.flip();
            byte[] packet = new byte[receiveBuffer.remaining()];
            receiveBuffer.get(packet);
            return packet;
        }
        return null;
    }

    private void processReceivedPacket(byte[] packet, List<byte[]> chunks) {
        logger.info("Получено " + packet.length + " байт");
        byte[] dataWithoutMarker = Arrays.copyOf(packet, packet.length - 1);
        chunks.add(dataWithoutMarker);
    }

    private byte[] combineChunks(List<byte[]> chunks) {
        return chunks.stream()
                .reduce(new byte[0], Bytes::concat);
    }

    private byte[] sendAndReceiveData(byte[] data) throws IOException {
        sendData(data);
        return receiveData();
    }

    public void close() {
        try {
            if (client != null && client.isOpen()) {
                client.close();
            }
        } catch (IOException e) {
            logger.error("Ошибка при закрытии клиента", e);
        }
    }
}