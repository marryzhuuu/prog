package share.network.requests;


import share.commands.CommandType;

public class RemoveRequest extends Request {
    public final int id;

    public RemoveRequest(int id) {
        super(CommandType.REMOVE_BY_ID);
        this.id = id;
    }
}
