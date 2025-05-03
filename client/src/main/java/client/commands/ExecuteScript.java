package client.commands;

import client.view.ConsoleView;
import share.commands.CommandType;

/**
 * Команда 'execute_script'. Выполняет скрипт из файла. Выполняется на клиентской стороне.
 */
public class ExecuteScript extends Command {
    private final ConsoleView console;

    public ExecuteScript(ConsoleView console) {
        super(CommandType.EXECUTE_SCRIPT, "выполнить скрипт из файла");
        this.console = console;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        if (arguments[1].isEmpty()) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + " script_file'");
            return false;
        }

        console.println("Выполнение скрипта '" + arguments[1] + "'...");
        return true;
    }
}
