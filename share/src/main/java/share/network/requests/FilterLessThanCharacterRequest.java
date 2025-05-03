package share.network.requests;


import share.commands.CommandType;
import share.model.DragonCharacter;

public class FilterLessThanCharacterRequest extends Request {
    public final DragonCharacter character;

    public FilterLessThanCharacterRequest(DragonCharacter character) {
        super(CommandType.FILTER_LESS_THAN_CHARACTER);
        this.character = character;
    }
}
