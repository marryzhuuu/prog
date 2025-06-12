package server.commands;

import server.collection.DragonCollection;
import share.commands.CommandType;
import share.network.requests.Request;
import share.network.responses.Response;
import share.network.responses.ShowResponse;

/**
 * Команда 'info'. Выводит информацию о коллекции.
 */
public class Show extends Command {
    private final DragonCollection collection;

    public Show(DragonCollection collection) {
        super(CommandType.SHOW);
        this.collection = collection;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        try {
            return new ShowResponse(collection.sorted(request.getUser()), null);
        } catch (Exception e) {
            return new ShowResponse(null, e.toString());
        }
  }
}
