package commands;

import Model.Color;
import Model.DragonCollection;
import View.ConsoleView;

import java.util.Map;

/**
 * Команда 'group_counting_by_color'. Группирует элементы по цвету.
 */
public class GroupCountingByColor extends Command {
    private final ConsoleView console;
    private final DragonCollection collection;

    public GroupCountingByColor(ConsoleView console, DragonCollection collection) {
        super("group_counting_by_color", "сгруппировать элементы по цвету");
        this.console = console;
        this.collection = collection;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        Map<Color, Long> groupedByColor = collection.groupCountingByColor();
        console.displayGroupedByColor(groupedByColor);
        return true;
    }
}
