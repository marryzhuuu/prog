package commands;

import Model.DragonCollection;
import View.ConsoleView;
import exceptions.WrongArgumentsAmount;

/**
 * Команда 'execute_script'. Выполняет скрипт из файла.
 */
public class ExecuteScript extends Command {
    private final ConsoleView console;

    public ExecuteScript(ConsoleView console) {
        super("execute_script file_name", "выполнить скрипт из файла");
        this.console = console;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        if (arguments[1].isEmpty()) {
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        console.println("Выполнение скрипта '" + arguments[1] + "'...");
        return true;
    }
}
