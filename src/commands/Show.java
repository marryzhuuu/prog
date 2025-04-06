package commands;

import Model.DragonCollection;
import View.ConsoleView;

/**
 * Команда 'show'. Выводит все элементы коллекции.
 */
public class Show extends Command {
    private final ConsoleView console;
    private final DragonCollection collection;

    public Show(ConsoleView console, DragonCollection collection) {
        super("show", "вывести все элементы коллекции");
        this.console = console;
        this.collection = collection;
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

        console.println(collection.getDragons());
        return true;
    }
}
