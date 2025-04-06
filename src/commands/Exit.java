package commands;

import View.ConsoleView;

/**
 * Команда 'exit'. Завершает выполнение программы.
 */
public class Exit extends Command {
    private final ConsoleView console;

    public Exit(ConsoleView console) {
        super("exit", "завершить программу");
        this.console = console;
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

        console.println("Завершение выполнения...");
        return true;
    }
}
