package server.commands;

import collection.DragonCollection;
import share.model.Dragon;
import share.network.requests.Request;
import share.network.responses.InfoResponse;
import share.network.responses.Response;
import share.network.responses.ShowResponse;

import java.util.Vector;

/**
 * Команда 'info'. Выводит информацию о коллекции.
 */
public class Show extends Command {
    private final DragonCollection collection;

    public Show(DragonCollection collection) {
        super("show", "вывести все элементы коллекции");
        this.collection = collection;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        try {
            Vector<Dragon> dragons = collection.getDragons();
            return new ShowResponse(dragons, null);
        } catch (Exception e) {
            return new ShowResponse(null, e.toString());
        }
  }
}
