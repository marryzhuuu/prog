package client.commands;

import client.auth.SessionHandler;
import client.network.UDPClient;
import client.view.ConsoleView;
import share.commands.CommandType;
import share.exceptions.APIException;
import share.exceptions.AuthentificationException;
import share.exceptions.NotFoundException;
import share.network.requests.RemoveRequest;
import share.network.responses.RemoveResponse;

import java.io.IOException;

/**
 * Команда 'show'. Удаляет элемент из коллекции.
 */
public class Remove extends Command {
    private final ConsoleView console;
    private final UDPClient client;

    public Remove(ConsoleView console, UDPClient client) {
        super( CommandType.REMOVE_BY_ID + " id", "удалить элемент по id");
        this.console = console;
        this.client = client;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        if (arguments[1].isEmpty()) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + " ID'");
            return false;
        }

        try {
            int id = Integer.parseInt(arguments[1].split(" ")[0]);

            // Запрос удалить дракона:
            var response = (RemoveResponse) client.sendAndReceiveCommand(new RemoveRequest(id, SessionHandler.getCurrentUser()));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }
            console.println("Дракон успешно удален");
            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (NumberFormatException e) {
            console.printError("ID элемента должен быть целым числом");
        } catch (AuthentificationException | APIException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}
