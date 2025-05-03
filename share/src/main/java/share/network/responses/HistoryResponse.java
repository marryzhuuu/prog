package share.network.responses;

import share.commands.CommandType;

public class HistoryResponse extends Response {
    public final String historyMessage;

    public HistoryResponse(String historyMessage, String error) {
        super(CommandType.INFO, error);
        this.historyMessage = historyMessage;
    }
}
