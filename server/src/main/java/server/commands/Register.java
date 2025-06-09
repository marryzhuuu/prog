package server.commands;

import org.postgresql.util.PSQLException;
import server.managers.AuthManager;
import share.commands.CommandType;
import share.network.requests.RegisterRequest;
import share.network.requests.Request;
import share.network.responses.RegisterResponse;
import share.network.responses.Response;

/**
 * Команда 'register'. Регистрирует пользователя.
 */
public class Register extends Command {
    private final AuthManager authManager;
    private final int MAX_USERNAME_LENGTH = 40;

    public Register(AuthManager authManager) {
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
        var req = (RegisterRequest) request;
        var user = req.getUser();
        if (user.getName().length() >= MAX_USERNAME_LENGTH) {
            return new RegisterResponse(user, "Длина имени пользователя не должна превышать " + MAX_USERNAME_LENGTH);
        }

        try {
            var newUserId = authManager.registerUser(user.getName(), user.getPassword());

            if (newUserId <= 0) {
                return new RegisterResponse(user, "Не удалось создать пользователя.");
            } else {
                return new RegisterResponse(user.copy(newUserId), null);
            }
        } catch (PSQLException e) {
            var message = "Ошибка PostgreSQL: " + e.getMessage();
            if (e.getMessage().contains("duplicate key value violates unique constraint \"users_name_key\"")) {
                message = "Неуникальное имя пользователя! Попробуйте другое.";
            }
            return new RegisterResponse(user, message);
        } catch (Exception e) {
            return new RegisterResponse(user, e.toString());
        }
    }
}