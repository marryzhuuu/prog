package share.network.requests;


import share.commands.CommandType;

public class SaveRequest extends Request {
    public SaveRequest() {
        super(CommandType.SAVE);
    }
}
