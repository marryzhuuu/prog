package server.commands;

import server.collection.DragonCollection;
import share.commands.CommandType;
import share.model.Color;
import share.network.requests.Request;
import share.network.responses.GroupCountingByColorResponse;
import share.network.responses.Response;

import java.util.Map;

/**
 * Команда 'group_counting_by_color'. Группирует элементы по цвету.
 */
public class GroupCountingByColor extends Command {
    private final DragonCollection collection;

    public GroupCountingByColor(DragonCollection collection) {
        super(CommandType.GROUP_COUNTING_BY_COLOR);
        this.collection = collection;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {

        Map<Color, Long> groupedByColor = collection.groupCountingByColor();
       var message = "Количество элементов по цвету:\n";
        for (java.util.Map.Entry<Color, Long> entry : groupedByColor.entrySet()) {
            message += entry.getKey() + ": " + entry.getValue() + '\n';
        }
        return new GroupCountingByColorResponse(message, null);
    }
}
