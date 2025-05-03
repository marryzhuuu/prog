package client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import client.controller.DragonController;
import client.view.ConsoleView;

/**
 * Главный класс клиентского приложения.
 */
public class Main {
    public static final Logger logger = LogManager.getLogger("ClientLogger");

    public static void main(String[] args) {
        ConsoleView consoleView = new ConsoleView();

        DragonController controller = new DragonController(consoleView, commandManager);

        consoleView.run(controller);
    }
}
