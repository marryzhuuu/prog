package server.commands;

import server.collection.DragonCollection;
import share.commands.CommandType;
import share.exceptions.ForbiddenException;
import share.exceptions.NotFoundException;
import share.model.Dragon;
import share.network.requests.Request;
import share.network.requests.UpdateRequest;
import share.network.responses.Response;
import share.network.responses.UpdateResponse;

/**
 * Команда 'info'. Добавляет элемент в коллекцию.
 */
public class Update extends Command {
    private final DragonCollection collection;

    public Update(DragonCollection collection) {
        super(CommandType.UPDATE);
        this.collection = collection;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var req = (UpdateRequest) request;
        try {
            Dragon updatedDragon = collection.updateDragon(req.getUser(), req.id, req.dragon);
            return new UpdateResponse(updatedDragon, null);
        } catch (ForbiddenException | NotFoundException e) {
            return new UpdateResponse(null, e.getMessage());
        }
        catch (Exception e) {
            return new UpdateResponse(null, e.toString());
        }
    }
}
