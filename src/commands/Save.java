package commands;

import Model.DragonCollection;
import View.ConsoleView;

import java.io.IOException;

/**
 * Команда 'save'. Сохраняет коллекцию в файл.
 */
public class Save extends Command {
    private final ConsoleView console;
    private final DragonCollection collection;

    public Save(ConsoleView console, DragonCollection collection) {
        super("save", "сохранить коллекцию в файл");
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

        collection.save();
        console.println("Коллекция сохранена");
        return true;
    }
}
