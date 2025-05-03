package share.network.requests;


import share.commands.CommandType;

public class GroupCountingByColorRequest extends Request {
    public GroupCountingByColorRequest() {
        super(CommandType.GROUP_COUNTING_BY_COLOR);
    }
}
