package client.commands;

import client.network.UDPClient;
import client.view.ConsoleView;
import share.network.requests.ShowRequest;
import share.network.responses.ShowResponse;

import java.io.IOException;

/**
 * Команда 'show'. Выводит все элементы коллекции.
 */
public class Show extends Command {
    private final ConsoleView console;
    private final UDPClient client;

    public Show(ConsoleView console, UDPClient client) {
        super("show");
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
            var response = (ShowResponse) client.sendAndReceiveCommand(new ShowRequest());
            for (var dragon : response.dragons) {
                console.println(dragon + "\n");
            }
            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        }
        return false;
    }
}
