package server.commands;

import server.collection.DragonCollection;
import share.commands.CommandType;
import share.network.requests.Request;
import share.network.responses.InfoResponse;
import share.network.responses.Response;

/**
 * Команда 'info'. Выводит информацию о коллекции.
 */
public class Info extends Command {
    private final DragonCollection collection;

    public Info(DragonCollection collection) {
        super(CommandType.INFO);
        this.collection = collection;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {

        var message = "Сведения о коллекции:\n" +
                " Тип: " + collection.getClass().getSimpleName() + "\n" +
                " Количество элементов: " + collection.size() + "\n" +
                " Дата последней инициализации: " + collection.getDate();
        return new InfoResponse(message, null);
    }
}
