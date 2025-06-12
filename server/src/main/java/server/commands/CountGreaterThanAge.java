package server.commands;

import server.collection.DragonCollection;
import share.commands.CommandType;
import share.network.requests.CountGreaterThanAgeRequest;
import share.network.requests.Request;
import share.network.responses.CountGreaterThanAgeResponse;
import share.network.responses.Response;
import share.network.responses.UpdateResponse;

/**
 * Команда 'info'. Добавляет элемент в коллекцию.
 */
public class CountGreaterThanAge extends Command {
    private final DragonCollection collection;

    public CountGreaterThanAge(DragonCollection collection) {
        super(CommandType.COUNT_GREATER_THAN_AGE);
        this.collection = collection;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var req = (CountGreaterThanAgeRequest) request;
        try {
            return new CountGreaterThanAgeResponse(collection.countGreaterThanAge(req.getUser(), req.age), null);
        } catch (Exception e) {
            return new UpdateResponse(null, e.toString());
        }
  }
}
