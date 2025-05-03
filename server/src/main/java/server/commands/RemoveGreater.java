package server.commands;

import server.collection.DragonCollection;
import share.commands.CommandType;
import share.network.requests.RemoveGreaterRequest;
import share.network.requests.Request;
import share.network.responses.AddIfMaxResponse;
import share.network.responses.RemoveGreaterResponse;
import share.network.responses.Response;

/**
 * Команда 'remove_greater'. Удаляет все элементы, старее введенного.
 */
public class RemoveGreater extends Command {
    private final DragonCollection collection;

    public RemoveGreater(DragonCollection collection) {
        super(CommandType.REMOVE_GREATER);
        this.collection = collection;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var req = (RemoveGreaterRequest) request;
        try {
            int size = collection.size();
            return new RemoveGreaterResponse(size - collection.removeGreater(req.dragon), null);
        } catch (Exception e) {
            return new AddIfMaxResponse(null, e.toString());
        }
  }
}
