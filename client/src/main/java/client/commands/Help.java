package client.commands;

import client.view.ConsoleView;
import share.commands.CommandType;

import java.util.HashMap;

/**
 * Команда 'help'. Выводит справку по доступным командам. Выполняется на стороне клиента.
 */
public class Help extends Command {
    private final ConsoleView console;
    private final HashMap<String, Command> commands;

    public Help(ConsoleView console, HashMap<String, Command> commands) {
        super(CommandType.HELP, "вывести справку по доступным командам");
        this.console = console;
        this.commands = commands;
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

        commands.values().forEach(command -> {
            console.println(command.getName() + ": " + command.getDescription());
        });
        return true;
    }
}
