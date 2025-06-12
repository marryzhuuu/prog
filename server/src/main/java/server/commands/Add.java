package server.commands;

import server.collection.DragonCollection;
import share.commands.CommandType;
import share.network.requests.AddRequest;
import share.network.requests.Request;
import share.network.responses.AddResponse;
import share.network.responses.Response;
import share.network.responses.ShowResponse;

/**
 * Команда 'info'. Добавляет элемент в коллекцию.
 */
public class Add extends Command {
    private final DragonCollection collection;

    public Add(DragonCollection collection) {
        super(CommandType.ADD);
        this.collection = collection;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var req = (AddRequest) request;
        try {
            collection.addDragon(req.getUser(), req.dragon);
            return new AddResponse(null);
        } catch (Exception e) {
            return new ShowResponse(null, e.toString());
        }
  }
}
