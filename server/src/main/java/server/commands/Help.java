package server.commands;

import share.network.requests.Request;
import share.network.responses.*;

/**
 * Команда 'help'. Вывести справку по командам
 */
public class Help extends Command {
    private final CommandManager commandManager;

    public Help(CommandManager commandManager) {
        super("help", "вывести справку по доступным командам");
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var helpMessage = new StringBuilder();

        commandManager.getCommands().values().forEach(command -> {
            helpMessage.append(" %-35s%-1s%n".formatted(command.getName(), command.getDescription()));
        });

        return new HelpResponse(helpMessage.toString(), null);
    }
}
