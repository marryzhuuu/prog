package commands;

import Model.Dragon;
import Model.DragonCollection;
import View.ConsoleView;

/**
 * Команда 'add'. Удаляет все элементы, старее введенного.
 */
public class RemoveGreater extends Command {
    private final ConsoleView console;
    private final DragonCollection collection;

    public RemoveGreater(ConsoleView console, DragonCollection collection) {
        super("remove_greater {element}", "удалить элементы, превышающие заданный по возрасту");
        this.console = console;
        this.collection = collection;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        try {
            console.println("* Ввод данных дракона:");
            Dragon greaterDragon = console.readDragon();
            int initialSize = collection.size();
            int newSize = collection.removeGreater(greaterDragon);
            console.println("Удалено " + (initialSize-newSize) + " элементов");
            return true;

        } catch (Exception e) {
            console.printError(e);
        }
        return false;
    }
}
