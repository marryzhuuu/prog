package client.commands;

import client.network.UDPClient;
import client.view.ConsoleView;
import share.network.requests.HelpRequest;
import share.network.responses.HelpResponse;

import java.io.IOException;

/**
 * Команда 'help'. Выводит справку по доступным командам
 */
public class Help extends Command {
    private final ConsoleView console;
    private final UDPClient client;

    public Help(ConsoleView console, UDPClient client) {
        super("help");
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
            var response = (HelpResponse) client.sendAndReceiveCommand(new HelpRequest());
            console.println(response.helpMessage);
            return true;
        } catch(IOException e) {
            console.printError("Ошибка обмена с сервером");
        }
        return false;
    }
}
