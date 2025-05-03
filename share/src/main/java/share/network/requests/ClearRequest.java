package share.network.requests;


import share.commands.CommandType;

public class ClearRequest extends Request {
    public ClearRequest() {
        super(CommandType.CLEAR);
    }
}
