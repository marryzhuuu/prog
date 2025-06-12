package share.network.requests;


import share.commands.CommandType;
import share.model.User;

public class GroupCountingByColorRequest extends Request {
    public GroupCountingByColorRequest(User user) {
        super(CommandType.GROUP_COUNTING_BY_COLOR, user);
    }
}
