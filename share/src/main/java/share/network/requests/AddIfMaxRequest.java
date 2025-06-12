package share.network.requests;


import share.commands.CommandType;
import share.model.Dragon;
import share.model.User;

public class AddIfMaxRequest extends Request {
    public final Dragon dragon;

    public AddIfMaxRequest(Dragon dragon, User user) {
        super(CommandType.ADD_IF_MAX, user);
        this.dragon = dragon;
    }
}
