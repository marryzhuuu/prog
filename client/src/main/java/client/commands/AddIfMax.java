package client.commands;

import client.auth.SessionHandler;
import client.builders.DragonBuilder;
import client.network.UDPClient;
import client.view.ConsoleView;
import share.commands.CommandType;
import share.exceptions.APIException;
import share.exceptions.AuthentificationException;
import share.network.requests.AddIfMaxRequest;
import share.network.responses.AddIfMaxResponse;

import java.io.IOException;

/**
 * Команда 'add_if_max'. Добавляет элемент, если он больше максимального по возрасту.
 */
public class AddIfMax extends Command {
    private final ConsoleView console;
    private final UDPClient client;

    public AddIfMax(ConsoleView console, UDPClient client) {
        super(CommandType.ADD_IF_MAX + " {element}", "добавить элемент, если он больше максимального по возрасту");
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
            var response = (AddIfMaxResponse) client.sendAndReceiveCommand(new AddIfMaxRequest(newDragon, SessionHandler.getCurrentUser()));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }
            if (response.dragon != null) {
                console.println("Добавлен дракон:\n" + response.dragon);
            } else {
                console.println("В коллекции есть драконы не младше");
            }
            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (APIException | AuthentificationException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}
