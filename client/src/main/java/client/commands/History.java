package client.commands;

import client.auth.SessionHandler;
import client.network.UDPClient;
import client.view.ConsoleView;
import share.commands.CommandType;
import share.exceptions.AuthentificationException;
import share.network.requests.HistoryRequest;
import share.network.responses.HistoryResponse;

import java.io.IOException;

/**
 * Команда 'history'. Выводит последние 15 команд, полученных сервером.
 */
public class History extends Command {
    private final ConsoleView console;
    private final UDPClient client;

    public History(ConsoleView console, UDPClient client) {
        super(CommandType.HISTORY, "вывести последние 15 команд");
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
            var response = (HistoryResponse) client.sendAndReceiveCommand(new HistoryRequest(SessionHandler.getCurrentUser()));
            console.println(response.historyMessage);
            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (AuthentificationException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}
