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
    private static final int PORT = 23586;

    public static void main(String[] args) {

        try {
            var client = new UDPClient(InetAddress.getLocalHost(), PORT);

            ConsoleView consoleView = new ConsoleView();
            DragonController controller = new DragonController(consoleView, client);

            consoleView.run(controller);

        } catch (IOException e) {
            logger.info("Невозможно подключиться к серверу.", e);
            System.out.println("Невозможно подключиться к серверу!");
        }





    }
}
