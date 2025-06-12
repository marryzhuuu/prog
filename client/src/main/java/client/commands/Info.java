package client.commands;

import client.auth.SessionHandler;
import client.network.UDPClient;
import client.view.ConsoleView;
import share.commands.CommandType;
import share.network.requests.InfoRequest;
import share.network.responses.InfoResponse;

import java.io.IOException;

/**
 * Команда 'info'. Выводит информацию о коллекции.
 */
public class Info extends Command {
    private final ConsoleView console;
    private final UDPClient client;

    public Info(ConsoleView console, UDPClient client) {
        super(CommandType.INFO, "вывести информацию о коллекции");
        this.console = console;
        this.client = client;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        if (!arguments[1].isEmpty()) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        try {
            var response = (InfoResponse) client.sendAndReceiveCommand(new InfoRequest(SessionHandler.getCurrentUser()));
            console.println(response.infoMessage);
            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        }
        return false;
    }
}
