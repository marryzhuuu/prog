package client;

import client.network.UDPClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import client.controller.DragonController;
import client.view.ConsoleView;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Главный класс клиентского приложения.
 */
public class Main {
    public static final Logger logger = LogManager.getLogger("ClientLogger");
    private static final int DEFAULT_PORT = 25001;

    public static void main(String[] args) {
        try {
            int port = args.length > 0 ? parsePort(args[0]) : DEFAULT_PORT;

            var client = new UDPClient(InetAddress.getLocalHost(), port);

            ConsoleView consoleView = new ConsoleView();
            DragonController controller = new DragonController(consoleView, client);

            consoleView.run(controller);

        } catch (IOException e) {
            logger.info("Невозможно подключиться к серверу.", e);
            System.out.println("Невозможно подключиться к серверу!");
        } catch (IllegalArgumentException e) {
            logger.error("Неверный номер порта", e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static int parsePort(String portStr) throws IllegalArgumentException {
        try {
            int port = Integer.parseInt(portStr);
            if (port < 0 || port > 65535) {
                throw new IllegalArgumentException("Номер порта должен быть в диапазоне от 0 до 65535");
            }
            return port;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Номер порта должен быть целым числом");
        }
    }
}