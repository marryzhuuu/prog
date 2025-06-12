package client.commands;

import client.auth.SessionHandler;
import client.builders.LoginBuilder;
import client.network.UDPClient;
import client.view.ConsoleView;
import share.commands.CommandType;
import share.exceptions.APIException;
import share.network.requests.LoginRequest;
import share.network.responses.LoginResponse;

import java.io.IOException;

/**
 * Команда 'login'. Аутентифицирует пользователя.
 */
public class Login extends Command {
    private final ConsoleView console;
    private final UDPClient client;

    public Login(ConsoleView console, UDPClient client) {
        super(CommandType.LOGIN + " {user}", "войти в аккаунт");
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
            console.println("* Вход в аккаунт:");
            var user = new LoginBuilder(console).build();
            var response = (LoginResponse) client.sendAndReceiveCommand(new LoginRequest(user));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }
            SessionHandler.setCurrentUser(response.user);
            console.println("Пользователь " + response.user.getName() + " успешно аутентифицирован");
            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (APIException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}
