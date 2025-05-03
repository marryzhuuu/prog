package server;

import collection.DragonCollection;
import collection.FileManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.commands.CommandManager;
import server.commands.*;
import server.network.UDPDatagramServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;


/**
 * Серверная часть приложения.
 */
public class Main {
    public static final int PORT = 23586;

    public static Logger logger = LogManager.getLogger("ServerLogger");

    public static void main(String[] args) throws IOException, ParseException {

        DragonCollection dragonCollection = new DragonCollection(new FileManager());


        var commandManager = new CommandManager() {{
            addCommand("help", new Help(this));
            addCommand("info", new Info(dragonCollection));
            addCommand("show", new Show(dragonCollection));
            // ToDo: остальные команды
        }};

        try {
            var server = new UDPDatagramServer(InetAddress.getLocalHost(), PORT, new CommandHandler(commandManager));
            // ToDo: сохранение коллекции перед завершением работы ссервера
            server.run();
        } catch (SocketException e) {
            logger.fatal("Ошибка сокета", e);
        } catch (UnknownHostException e) {
            logger.fatal("Неизвестный хост", e);
        }

    }
}
