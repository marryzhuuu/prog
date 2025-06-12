package server.commands;

import server.collection.DragonCollection;
import share.commands.CommandType;
import share.network.requests.AddIfMaxRequest;
import share.network.requests.Request;
import share.network.responses.AddIfMaxResponse;
import share.network.responses.Response;

/**
 * Команда 'info'. Добавляет элемент в коллекцию.
 */
public class AddIfMax extends Command {
    private final DragonCollection collection;

    public AddIfMax(DragonCollection collection) {
        super(CommandType.ADD_IF_MAX);
        this.collection = collection;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var req = (AddIfMaxRequest) request;
        try {
            return new AddIfMaxResponse(collection.addIfMax(req.getUser(), req.dragon), null);
        } catch (Exception e) {
            return new AddIfMaxResponse(null, e.toString());
        }
  }
}
