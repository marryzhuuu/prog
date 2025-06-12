package share.network.requests;


import share.commands.CommandType;
import share.model.User;

public class ShowRequest extends Request {
    public ShowRequest(User user) {
        super(CommandType.SHOW, user);
    }
}
