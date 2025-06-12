package share.network.requests;


import share.commands.CommandType;
import share.model.User;

public class SaveRequest extends Request {
    public SaveRequest(User user) {
        super(CommandType.SAVE, user);
    }
}
