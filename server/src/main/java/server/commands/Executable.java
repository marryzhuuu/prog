package server.commands;

import share.network.requests.Request;
import share.network.responses.Response;

/**
 * Интерфейс для выполняемых команд.
 */
public interface Executable {
    /**
     * Выполнить команду.
     * @param request запрос для выполнения команды
     * @return результат выполнения
     */
    Response apply(Request request);
}
