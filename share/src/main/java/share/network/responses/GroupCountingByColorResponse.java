package share.network.responses;

import share.commands.CommandType;

public class GroupCountingByColorResponse extends Response {
    public final String message;

    public GroupCountingByColorResponse(String message, String error) {
        super(CommandType.GROUP_COUNTING_BY_COLOR, error);
        this.message = message;
    }
}
