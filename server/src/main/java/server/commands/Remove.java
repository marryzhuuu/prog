package server.commands;

import server.collection.DragonCollection;
import share.commands.CommandType;
import share.exceptions.ForbiddenException;
import share.exceptions.NotFoundException;
import share.network.requests.RemoveRequest;
import share.network.requests.Request;
import share.network.responses.RemoveResponse;
import share.network.responses.Response;

/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции.
 */
public class Remove extends Command {
    private final DragonCollection collection;

    public Remove(DragonCollection collection) {
        super(CommandType.REMOVE_BY_ID);
        this.collection = collection;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var req = (RemoveRequest) request;
        try {
            collection.removeDragon(req.getUser(), req.id);
            return new RemoveResponse(null);
        } catch (NotFoundException | ForbiddenException e) {
            return new RemoveResponse(e.toString());
        }
    }
}
