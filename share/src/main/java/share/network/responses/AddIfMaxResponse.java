package share.network.responses;

import share.commands.CommandType;
import share.model.Dragon;

public class AddIfMaxResponse extends Response {
    public final Dragon dragon;

    public AddIfMaxResponse(Dragon dragon, String error) {

        super(CommandType.ADD_IF_MAX, error);
        this.dragon = dragon;
    }
}
