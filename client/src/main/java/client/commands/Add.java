package client.commands;

import client.auth.SessionHandler;
import client.builders.DragonBuilder;
import client.network.UDPClient;
import client.view.ConsoleView;
import share.commands.CommandType;
import share.exceptions.APIException;
import share.network.requests.AddRequest;
import share.network.responses.AddResponse;

import java.io.IOException;

/**
 * Команда 'add'. Добавляет элемент в коллекцию.
 */
public class Add extends Command {
    private final ConsoleView console;
    private final UDPClient client;

    public Add(ConsoleView console, UDPClient client) {
        super(CommandType.ADD + " {element}", "добавить элемент в коллекцию");
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
            console.println("* Создание нового дракона:");

            var newDragon = new DragonBuilder(console).build();
            var response = (AddResponse) client.sendAndReceiveCommand(new AddRequest(newDragon, SessionHandler.getCurrentUser()));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }
            console.println("Дракон успешно добавлен");
            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (APIException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}
