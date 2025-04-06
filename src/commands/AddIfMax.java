package commands;

import model.Dragon;
import model.DragonCollection;
import view.ConsoleView;

/**
 * Команда 'add'. Добавляет элемент, если он больше максимального по возрасту.
 */
public class AddIfMax extends Command {
    private final ConsoleView console;
    private final DragonCollection collection;

    public AddIfMax(ConsoleView console, DragonCollection collection) {
        super("add {element}", "добавить элемент, если он больше максимального по возрасту");
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
            console.println("* Добавление нового дракона:");
            Dragon newDragon = console.readDragon();
            Dragon addedDragon = collection.addIfMax(newDragon);
            if(addedDragon != null) {
                console.println("Добавлен элемент:\n" + addedDragon);
            }
            else {
                console.println("В коллекции есть драконы не младше");
            }
            return true;

        } catch (Exception e) {
            console.printError(e);
        }
        return false;
    }
}
