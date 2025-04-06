package commands;

import model.Dragon;
import model.DragonCollection;
import view.ConsoleView;
import exceptions.NotFoundException;
import exceptions.WrongArgumentsAmount;

/**
 * Команда 'remove_by_id'. Удаляет элемент по индексу в коллекции.
 */
public class RemoveById extends Command {
    private final ConsoleView console;
    private final DragonCollection collection;

    public RemoveById(ConsoleView console, DragonCollection collection) {
        super("remove_by_id id {element}", "удалить элемент по id");
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
            int id = Integer.parseInt(arguments[1].split(" ")[0]);
            Dragon dragon = collection.findDragonById(id);
            if (dragon == null) {
                throw new NotFoundException();
            }
            collection.removeDragon(id);
            console.println("Дракон успешно удален!");
            return true;
        }
        catch (WrongArgumentsAmount e) {
            console.printError("Неправильное количество аргументов!");
        }
        catch (NotFoundException e) {
            console.printError("Элемент не найден!");
        }
        catch (Exception e) {
            console.printError(e);
        }
        return false;
    }
}
