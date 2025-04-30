package client.commands;


import client.model.DragonCollection;
import client.view.ConsoleView;

/**
 * Команда 'clear'. Очищает коллекцию.
 * @author maxbarsukov
 */
public class Clear extends Command {
    private final ConsoleView console;
    private final DragonCollection collection;

    public Clear(ConsoleView console, DragonCollection collection) {
        super("clear", "очистить коллекцию");
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

        collection.clear();
        console.println("Коллекция очищена!");
        return true;
    }
}
