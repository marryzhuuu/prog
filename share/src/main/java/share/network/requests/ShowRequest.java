package share.network.requests;


import share.commands.CommandType;

public class ShowRequest extends Request {
    public ShowRequest() {
        super(CommandType.SHOW);
    }
}
