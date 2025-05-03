package client.commands;

import client.model.DragonCollection;
import client.view.ConsoleView;
import client.builders.DragonBuilder;

/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class Add extends Command {
    private final ConsoleView console;
    private final DragonCollection collection;

    public Add(ConsoleView console, DragonCollection collection) {
        super("add {element}", "добавить новый элемент в коллекцию");
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
            console.println("* Создание нового дракона:");
            collection.addDragon(new DragonBuilder(console).build());
            console.println("Дракон успешно добавлен!");
            return true;

        } catch (Exception e) {
            console.printError(e);
        }
        return false;
    }
}
