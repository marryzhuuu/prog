package share.network.requests;


import share.commands.CommandType;
import share.model.User;

public class HistoryRequest extends Request {
    public HistoryRequest(User user) {
        super(CommandType.HISTORY, user);
    }
}
