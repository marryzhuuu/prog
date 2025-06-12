package share.network.requests;


import share.commands.CommandType;
import share.model.Dragon;
import share.model.User;

public class RemoveGreaterRequest extends Request {
    public final Dragon dragon;

    public RemoveGreaterRequest(Dragon dragon, User user) {
        super(CommandType.REMOVE_GREATER, user);
        this.dragon = dragon;
    }
}
