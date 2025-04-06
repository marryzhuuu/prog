package commands;

import Model.Dragon;
import Model.DragonCollection;
import View.ConsoleView;

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
                console.showMessage("Добавлен элемент:\n" + addedDragon);
            }
            else {
                console.showMessage("В коллекции есть драконы не младше");
            }
            return true;

        } catch (Exception e) {
            console.printError(e);
        }
        return false;
    }
}
