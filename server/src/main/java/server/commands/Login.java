package server.commands;

import server.managers.AuthManager;
import share.commands.CommandType;
import share.exceptions.WrongPasswordException;
import share.network.requests.LoginRequest;
import share.network.requests.Request;
import share.network.responses.LoginResponse;
import share.network.responses.Response;

/**
 * Команда 'login'. Аутентифицирует пользователя.
 */
public class Login extends Command {
    private final AuthManager authManager;

    public Login(AuthManager authManager) {
        super(CommandType.REGISTER);
        this.authManager = authManager;
    }

    /**
     * Выполняет команду
     * @param request Запрос к серверу.
     * @return Ответ сервера.
     */
    @Override
    public Response apply(Request request) {
        var req = (LoginRequest) request;
        var user = req.getUser();
        try {
            var userId = authManager.loginUser(user.getName(), user.getPassword());

            if (userId <= 0) {
                return new LoginResponse(user, "Пользователь " + user.getName() + " не существует.");
            } else {
                return new LoginResponse(user.copy(userId), null);
            }
        } catch (WrongPasswordException e) {
            return new LoginResponse(user, e.getMessage());
        } catch (Exception e) {
            return new LoginResponse(user, e.toString());
        }
    }
}