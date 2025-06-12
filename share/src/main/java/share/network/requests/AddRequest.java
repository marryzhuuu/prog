package share.network.requests;


import share.commands.CommandType;
import share.model.Dragon;
import share.model.User;

public class AddRequest extends Request {
    public final Dragon dragon;

    public AddRequest(Dragon dragon, User user) {
        super(CommandType.ADD, user);
        this.dragon = dragon;
    }
}
