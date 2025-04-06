package commands;

import View.ConsoleView;

/**
 * Команда 'help'. Выводит справку по доступным командам
 */
public class Help extends Command {
    private final ConsoleView console;
    private final CommandManager commandManager;

    public Help(ConsoleView console, CommandManager commandManager) {
        super("help", "вывести справку по доступным командам");
        this.console = console;
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        if (!arguments[1].isEmpty()) {
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        commandManager.getCommands().values().forEach(command -> {
            console.println(command.getName() + ": " + command.getDescription());
        });
        return true;
    }
}
