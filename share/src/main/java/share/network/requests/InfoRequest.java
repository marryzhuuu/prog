package share.network.requests;


import share.commands.CommandType;
import share.model.User;

public class InfoRequest extends Request {
    public InfoRequest(User user) {
        super(CommandType.INFO, user);
    }
}
