package share.network.responses;

import share.commands.CommandType;

public class ClearResponse extends Response {
    public ClearResponse(String error) {
        super(CommandType.CLEAR, error);
    }
}
