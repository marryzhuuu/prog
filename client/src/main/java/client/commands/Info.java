package client.commands;

import client.model.DragonCollection;
import client.view.ConsoleView;

/**
 * Команда 'info'. Выводит информацию о коллекции.
 */
public class Info extends Command {
    private final ConsoleView console;
    private final DragonCollection collection;

    public Info(ConsoleView console, DragonCollection collection) {
        super("info", "вывести информацию о коллекции");
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

        console.println("Сведения о коллекции:");
        System.out.println("Тип коллекции: " + collection.getClass().getSimpleName());
        System.out.println("Дата инициализации: " + collection.getDate());
        System.out.println("Количество элементов: " + collection.size());
        return true;
    }
}
