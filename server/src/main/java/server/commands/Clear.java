package server.commands;

import server.collection.DragonCollection;
import share.commands.CommandType;
import share.network.requests.Request;
import share.network.responses.ClearResponse;
import share.network.responses.Response;

/**
 * Команда 'clear'. Удаляет все элементы коллекции.
 */
public class Clear extends Command {
    private final DragonCollection collection;

    public Clear(DragonCollection collection) {
        super(CommandType.CLEAR);
        this.collection = collection;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        try {
            collection.clear();
            return new ClearResponse(null);
        } catch (Exception e) {
            return new ClearResponse(e.toString());
        }
  }
}
