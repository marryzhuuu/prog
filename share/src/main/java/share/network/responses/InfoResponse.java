package share.network.responses;

import share.commands.CommandType;

public class InfoResponse extends Response {
    public final String infoMessage;

    public InfoResponse(String infoMessage, String error) {
        super(CommandType.INFO, error);
        this.infoMessage = infoMessage;
    }
}
