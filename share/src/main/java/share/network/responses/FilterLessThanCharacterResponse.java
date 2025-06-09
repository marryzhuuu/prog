package share.network.responses;

import share.commands.CommandType;
import share.model.Dragon;

import java.util.List;

public class FilterLessThanCharacterResponse extends Response {
    public final List<Dragon> dragons;

    public FilterLessThanCharacterResponse(List<Dragon> dragons, String error) {
        super(CommandType.FILTER_LESS_THAN_CHARACTER, error);
        this.dragons = dragons;
    }
}
