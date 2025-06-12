package share.network.requests;


import share.commands.CommandType;
import share.model.DragonCharacter;
import share.model.User;

public class FilterLessThanCharacterRequest extends Request {
    public final DragonCharacter character;

    public FilterLessThanCharacterRequest(DragonCharacter character, User user) {
        super(CommandType.FILTER_LESS_THAN_CHARACTER, user);
        this.character = character;
    }
}
