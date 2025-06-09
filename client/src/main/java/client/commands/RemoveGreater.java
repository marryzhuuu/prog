package client.commands;

import client.builders.DragonBuilder;
import client.network.UDPClient;
import client.view.ConsoleView;
import share.commands.CommandType;
import share.exceptions.APIException;
import share.network.requests.RemoveGreaterRequest;
import share.network.responses.RemoveGreaterResponse;

import java.io.IOException;

/**
 * Команда 'remove_greater'. Удаляет все элементы, старее введенного.
 */
public class RemoveGreater extends Command {
    private final ConsoleView console;
    private final UDPClient client;

    public RemoveGreater(ConsoleView console, UDPClient client) {
        super(CommandType.REMOVE_GREATER + " {element}", "удалить элементы, превышающие заданный по возрасту");
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
            console.println("* Ввод данных дракона:");

            var newDragon = new DragonBuilder(console).build();
            var response = (RemoveGreaterResponse) client.sendAndReceiveCommand(new RemoveGreaterRequest(newDragon));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }
            console.println("Удалено " + response.deleted + " драконов" );
            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (APIException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}
