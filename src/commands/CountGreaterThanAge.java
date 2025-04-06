package commands;

import model.DragonCollection;
import view.ConsoleView;
import exceptions.WrongArgumentsAmount;

/**
 * Команда 'count_greater_than_age'. Выводит количество элементов с age больше заданного.
 */
public class CountGreaterThanAge extends Command {
    private final ConsoleView console;
    private final DragonCollection collection;

    public CountGreaterThanAge(ConsoleView console, DragonCollection collection) {
        super("count_greater_than_age age", "вывести количество элементов с age больше заданного");
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
            if (arguments[1].isEmpty()) throw new WrongArgumentsAmount();
            long age = Long.parseLong(arguments[1].split(" ")[0]);
            long count = collection.countGreaterThanAge(age);
            console.println("Количество элементов старше " + age + ": " + count);
            return true;
        }
        catch (WrongArgumentsAmount e) {
            console.printError("Неправильное количество аргументов!");
        } catch (Exception e) {
            console.printError(e);
        }
        return false;
    }
}
