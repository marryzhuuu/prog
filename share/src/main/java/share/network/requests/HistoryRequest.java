package share.network.requests;


import share.commands.CommandType;

public class HistoryRequest extends Request {
    public HistoryRequest() {
        super(CommandType.HISTORY);
    }
}
