package share.network.responses;

import share.commands.CommandType;

public class RemoveResponse extends Response {
    public RemoveResponse(String error) {
        super(CommandType.REMOVE_BY_ID, error);
    }
}
