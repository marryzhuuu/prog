package server.commands;

import server.collection.DragonCollection;
import share.commands.CommandType;
import share.network.requests.FilterLessThanCharacterRequest;
import share.network.requests.Request;
import share.network.responses.FilterLessThanCharacterResponse;
import share.network.responses.Response;

/**
 * Команда 'filter_less_than_character'. Выводит элементы с character меньше заданного.
 */
public class FilterLessThanCharacter extends Command {
    private final DragonCollection collection;

    public FilterLessThanCharacter(DragonCollection collection) {
        super(CommandType.FILTER_LESS_THAN_CHARACTER);
        this.collection = collection;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var req = (FilterLessThanCharacterRequest) request;
        try {

            return new FilterLessThanCharacterResponse(collection.filterLessThanCharacter(req.character), null);
        } catch (Exception e) {
            return new FilterLessThanCharacterResponse(null, e.toString());
        }
  }
}
