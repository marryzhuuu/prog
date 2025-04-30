package client.commands;

import client.model.Dragon;
import client.model.DragonCharacter;
import client.model.DragonCollection;
import client.view.ConsoleView;
import share.exceptions.WrongArgumentsAmount;

import java.util.List;

/**
 * Команда 'filter_less_than_character'. Выводит элементы с character меньше заданного.
 */
public class FilterLessThanCharacter extends Command {
    private final ConsoleView console;
    private final DragonCollection collection;

    public FilterLessThanCharacter(ConsoleView console, DragonCollection collection) {
        super("filter_less_than_character character", "вывести элементы с character меньше заданного");
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
            String characterString = arguments[1].split(" ")[0];
            DragonCharacter character = DragonCharacter.valueOf(characterString.toUpperCase());
            List<Dragon> filteredDragons = collection.filterLessThanCharacter(character);
            console.println("Найденные элементы: ");
            console.displayElementList(filteredDragons);
            return true;
        }
        catch (WrongArgumentsAmount e) {
            console.printError("Неправильное количество аргументов!");
        }
        catch (Exception e) {
            console.printError("Введите корректный характер (CUNNING, EVIL, CHAOTIC_EVIL, FICKLE, GOOD)");
        }
        return false;
    }
}
