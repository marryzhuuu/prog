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
    public static final int PORT = 23586;

    public static Logger logger = LogManager.getLogger("ServerLogger");

    public static void main(String[] args) throws IOException, ParseException {

        DragonCollection dragonCollection = new DragonCollection(new FileManager());

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
