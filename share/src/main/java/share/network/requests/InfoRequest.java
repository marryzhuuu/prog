package share.network.requests;


import share.commands.CommandType;

public class InfoRequest extends Request {
    public InfoRequest() {
        super(CommandType.INFO);
    }
}
