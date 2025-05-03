package server.commands;

import share.network.requests.Request;
import share.network.responses.Response;
import share.network.responses.UnknownCommandResponse;

public class CommandHandler {
    private final CommandManager manager;

    public CommandHandler(CommandManager manager) {
        this.manager = manager;
    }

    public Response handle(Request request) {
        var command = manager.getCommands().get(request.getName());
        if (command == null) return new UnknownCommandResponse(request.getName());
        return command.apply(request);
    }
}
