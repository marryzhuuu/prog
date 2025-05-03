package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Серверная часть приложения.
 */
public class Main {
    public static final int PORT = 23586;

    public static Logger logger = LogManager.getLogger("ServerLogger");

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Введите имя загружаемого файла как аргумент командной строки");
            System.exit(1);
        }

        System.out.println("Arguments: " + args[0]);
        logger.debug("Arguments: " + args[0]);

    }
}
