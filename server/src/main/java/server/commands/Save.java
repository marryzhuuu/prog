package server.commands;

import server.collection.DragonCollection;
import share.commands.CommandType;
import share.network.requests.Request;
import share.network.responses.Response;
import share.network.responses.SaveResponse;

/**
 * Команда 'save'. Сохраняет колекцию в дамп.
 */
public class Save extends Command {
    private final DragonCollection collection;

    public Save(DragonCollection collection) {
        super(CommandType.SAVE);
        this.collection = collection;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        try {
            collection.save();
            return new SaveResponse(null);
        } catch (Exception e) {
            return new SaveResponse(e.toString());
        }
  }
}
