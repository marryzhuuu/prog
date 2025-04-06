package commands;

import Model.Dragon;
import Model.DragonCollection;
import View.ConsoleView;
import exceptions.NotFoundException;
import exceptions.WrongArgumentsAmount;

/**
 * Команда 'update'. Изменяет элемент по индексу в коллекции.
 */
public class Update extends Command {
    private final ConsoleView console;
    private final DragonCollection collection;

    public Update(ConsoleView console, DragonCollection collection) {
        super("update id {element}", "обновить элемент по id");
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
            console.println("* Изменение дракона:");
            int id = Integer.parseInt(arguments[1].split(" ")[0]);
            Dragon dragon = collection.findDragonById(id);
            if (dragon == null) {
                throw new NotFoundException();
            }
            Dragon updatedDragon = console.readDragonParams(dragon);
            dragon = collection.updateDragon(id, updatedDragon);
            console.showMessage("\nОбновленный дракон:\n" + dragon);
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
