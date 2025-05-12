package server;

import server.collection.DragonCollection;
import server.collection.FileManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.commands.CommandManager;
import server.commands.*;
import server.network.UDPDatagramServer;
import share.commands.CommandType;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;


/**
 * Серверная часть приложения.
 */
public class Main {
    private static final int DEFAULT_PORT = 25001;



    private static DragonCollection dragonCollection; // статический для использования в shutdown hook

    public static Logger logger = LogManager.getLogger("ServerLogger");

    public static void main(String[] args) throws IOException, ParseException {



        dragonCollection = new DragonCollection(new FileManager());

        // Добавляем shutdown hook для сохранения коллекции при завершении работы сервера
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                logger.info("Сервер завершает работу, сохраняем коллекцию...");
                dragonCollection.save();
                logger.info("Коллекция успешно сохранена");
            } catch (Exception e) {
                logger.error("Ошибка при сохранении коллекции при завершении работы", e);
            }
        }));

        var commandManager = new CommandManager() {{
            addCommand(CommandType.INFO, new Info(dragonCollection));
            addCommand(CommandType.SHOW, new Show(dragonCollection));
            addCommand(CommandType.ADD, new Add(dragonCollection));
            addCommand(CommandType.GET, new GetById(dragonCollection));
            addCommand(CommandType.UPDATE, new Update(dragonCollection));
            addCommand(CommandType.REMOVE_BY_ID, new Remove(dragonCollection));
            addCommand(CommandType.CLEAR, new Clear(dragonCollection));
            addCommand(CommandType.SAVE, new Save(dragonCollection));
            addCommand(CommandType.ADD_IF_MAX, new AddIfMax(dragonCollection));
            addCommand(CommandType.REMOVE_GREATER, new RemoveGreater(dragonCollection));
            addCommand(CommandType.HISTORY, new History(this));
            addCommand(CommandType.GROUP_COUNTING_BY_COLOR, new GroupCountingByColor(dragonCollection));
            addCommand(CommandType.COUNT_GREATER_THAN_AGE, new CountGreaterThanAge(dragonCollection));
            addCommand(CommandType.FILTER_LESS_THAN_CHARACTER, new FilterLessThanCharacter(dragonCollection));
        }};

        try {
            int port = args.length > 0 ? parsePort(args[0]) : DEFAULT_PORT;
            var server = new UDPDatagramServer(InetAddress.getLocalHost(), port, new CommandHandler(commandManager));

            server.run();
        } catch (SocketException e) {
            logger.fatal("Ошибка сокета", e);
        } catch (UnknownHostException e) {
            logger.fatal("Неизвестный хост", e);
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
