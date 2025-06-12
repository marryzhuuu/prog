package server.commands;

import server.collection.DragonCollection;
import share.commands.CommandType;
import share.network.requests.GetByIdRequest;
import share.network.requests.Request;
import share.network.responses.GetByIdResponse;
import share.network.responses.Response;

/**
 * Команда 'get'. Служебная команда. Получает элемент коллекции по id.
 */
public class GetById extends Command {
    private final DragonCollection collection;

    public GetById(DragonCollection collection) {
        super(CommandType.GET + "get");
        this.collection = collection;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        try {
            var req = (GetByIdRequest) request;
            return new GetByIdResponse(collection.findDragonById(req.getUser(), req.id), null);
        } catch (Exception e) {
            return new GetByIdResponse(null, e.toString());
        }
  }
}
