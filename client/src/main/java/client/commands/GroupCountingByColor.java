package client.commands;

import client.auth.SessionHandler;
import client.network.UDPClient;
import client.view.ConsoleView;
import share.commands.CommandType;
import share.exceptions.AuthentificationException;
import share.network.requests.GroupCountingByColorRequest;
import share.network.responses.GroupCountingByColorResponse;

import java.io.IOException;

/**
 * Команда 'group_counting_by_color'. Группирует элементы по цвету.
 */
public class GroupCountingByColor extends Command {
    private final ConsoleView console;
    private final UDPClient client;

    public GroupCountingByColor(ConsoleView console, UDPClient client) {
        super(CommandType.GROUP_COUNTING_BY_COLOR, "сгруппировать элементы по цвету");
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
            var response = (GroupCountingByColorResponse) client.sendAndReceiveCommand(new GroupCountingByColorRequest(SessionHandler.getCurrentUser()));
            console.println(response.message);
            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (AuthentificationException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}
