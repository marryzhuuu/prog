package client.commands;

import client.builders.RegisterBuilder;
import client.network.UDPClient;
import client.view.ConsoleView;
import share.commands.CommandType;
import share.exceptions.APIException;
import share.network.requests.RegisterRequest;
import share.network.responses.RegisterResponse;

import java.io.IOException;

/**
 * Команда 'register'. Добавляет пользователя.
 */
public class Register extends Command {
    private final ConsoleView console;
    private final UDPClient client;

    public Register(ConsoleView console, UDPClient client) {
        super(CommandType.REGISTER + " {user}", "зарегистрировать пользователя");
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
            console.println("* Регистрация пользователя:");

            var user = new RegisterBuilder(console).build();
            var response = (RegisterResponse) client.sendAndReceiveCommand(new RegisterRequest(user));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }
            console.println("Пользователь " + response.user.getName() + " успешно добавлен");
            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (APIException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}
