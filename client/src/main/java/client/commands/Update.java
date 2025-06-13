package client.commands;

import client.auth.SessionHandler;
import client.builders.DragonBuilder;
import client.network.UDPClient;
import client.view.ConsoleView;
import share.commands.CommandType;
import share.exceptions.APIException;
import share.exceptions.AuthentificationException;
import share.exceptions.NotFoundException;
import share.model.Dragon;
import share.network.requests.GetByIdRequest;
import share.network.requests.UpdateRequest;
import share.network.responses.GetByIdResponse;
import share.network.responses.UpdateResponse;

import java.io.IOException;

/**
 * Команда 'show'. Изменяет элемент в коллекции.
 */
public class Update extends Command {
    private final ConsoleView console;
    private final UDPClient client;

    public Update(ConsoleView console, UDPClient client) {
        super( CommandType.UPDATE + " id {element}", "обновить элемент по id");
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
            console.println("* Изменение дракона:");
            int id = Integer.parseInt(arguments[1].split(" ")[0]);

            // Получить дракона из коллекции:
            var getResponse = (GetByIdResponse) client.sendAndReceiveCommand(new GetByIdRequest(id, SessionHandler.getCurrentUser()));
            Dragon dragon = getResponse.dragon;
            if (dragon == null) {
                throw new NotFoundException();
            }
            // Получить данные для обновления:
            Dragon updatedDragon = new DragonBuilder(console).update(dragon);
            // Запрос обновить дракона:
            var updateResponse = (UpdateResponse) client.sendAndReceiveCommand(new UpdateRequest(id, updatedDragon, SessionHandler.getCurrentUser()));
            if (updateResponse.getError() != null && !updateResponse.getError().isEmpty()) {
                throw new APIException(updateResponse.getError());
            }
            dragon = updateResponse.dragon;
            console.println("\nОбновленный дракон:\n" + dragon);
            console.println("Дракон успешно обновлен");
            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (NotFoundException e) {
            console.printError("Элемент не найден");
        } catch (NumberFormatException e) {
            console.printError("ID элемента должен быть целым числом");
        } catch (AuthentificationException | APIException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}
