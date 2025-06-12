package share.network.requests;


import share.commands.CommandType;
import share.model.User;

public class RemoveRequest extends Request {
    public final int id;

    public RemoveRequest(int id, User user) {
        super(CommandType.REMOVE_BY_ID, user);
        this.id = id;
    }
}
