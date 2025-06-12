package share.network.requests;


import share.commands.CommandType;
import share.model.User;

public class ClearRequest extends Request {
    public ClearRequest(User user) {
        super(CommandType.CLEAR, user);
    }
}
