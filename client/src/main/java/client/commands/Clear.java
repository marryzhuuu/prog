package client.commands;

import client.auth.SessionHandler;
import client.network.UDPClient;
import client.view.ConsoleView;
import share.commands.CommandType;
import share.exceptions.AuthentificationException;
import share.network.requests.ClearRequest;

import java.io.IOException;

/**
 * Команда 'clear'. Удаляет все элементы коллекции.
 */
public class Clear extends Command {
    private final ConsoleView console;
    private final UDPClient client;

    public Clear(ConsoleView console, UDPClient client) {
        super(CommandType.CLEAR, "удалить все элементы коллекции");
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
            client.sendAndReceiveCommand(new ClearRequest(SessionHandler.getCurrentUser()));
            console.println("Коллекция очищена");
            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (AuthentificationException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}
