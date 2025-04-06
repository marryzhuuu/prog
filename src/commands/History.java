package commands;

import View.ConsoleView;

/**
 * Команда 'history'. Выводит последние 15 команд
 */
public class History extends Command {
    private final ConsoleView console;
    private final CommandManager commandManager;

    public History(ConsoleView console, CommandManager commandManager) {
        super("history", "вывести последние 15 команд");
        this.console = console;
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        System.out.println("Последние " + CommandManager.getHistorySize() + " команд:");
        for (String cmd : commandManager.getCommandHistory()) {
            System.out.println(cmd.split(" ")[0]); // Выводим только команду без аргументов
        }
        return true;
    }
}
