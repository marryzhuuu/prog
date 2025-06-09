package server.commands;

import server.managers.CommandManager;
import share.commands.CommandType;
import share.network.requests.Request;
import share.network.responses.HistoryResponse;
import share.network.responses.Response;

/**
 * Команда 'history'. Выводит последние 15 команд.
 */
public class History extends Command {
    private final CommandManager manager;

    public History(CommandManager manager) {
        super(CommandType.HISTORY);
        this.manager = manager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {

        var history = manager.getCommandHistory();
        var historyMessage = "Последние команды:\n" + String.join("\n", history);

        return new HistoryResponse(historyMessage, null);
    }
}
