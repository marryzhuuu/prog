package commands;

import exceptions.SaveToFileException;
import model.DragonCollection;
import view.ConsoleView;

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

        try {
            collection.save();
        }
        catch (SaveToFileException e) {
            console.printError("Ошибка сохранения в файл!");
        }
        console.println("Коллекция сохранена");
        return true;
    }
}
