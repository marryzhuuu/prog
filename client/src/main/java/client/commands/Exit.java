package client.commands;

import client.view.ConsoleView;
import share.commands.CommandType;

/**
 * Команда 'exit'. Завершает выполнение клиента.
 */
public class Exit extends Command {
    private final ConsoleView console;

    public Exit(ConsoleView console) {
        super(CommandType.EXIT, "завершить выполнение клиента");
        this.console = console;
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

        console.println("Завершение выполнения клиента...");
        return true;
    }
}
