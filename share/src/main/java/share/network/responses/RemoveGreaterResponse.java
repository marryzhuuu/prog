package share.network.responses;

import share.commands.CommandType;

public class RemoveGreaterResponse extends Response {
    public final int deleted;

    public RemoveGreaterResponse(int deleted, String error) {

        super(CommandType.REMOVE_GREATER, error);
        this.deleted = deleted;
    }
}
