package share.network.requests;


import share.commands.CommandType;
import share.model.Dragon;
import share.model.User;

public class UpdateRequest extends Request {
    public final Dragon dragon;
    public final int id;

    public UpdateRequest(int id, Dragon dragon, User user) {
        super(CommandType.UPDATE, user);
        this.dragon = dragon;
        this.id = id;
    }
}
