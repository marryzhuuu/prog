package share.network.responses;

import share.commands.CommandType;
import share.model.Dragon;

public class UpdateResponse extends Response {
    public final Dragon dragon;

    public UpdateResponse(Dragon dragon, String error)
    {
        super(CommandType.UPDATE, error);
        this.dragon = dragon;
    }
}
