package share.network.responses;

import share.commands.CommandType;

public class SaveResponse extends Response {
    public SaveResponse(String error) {
        super(CommandType.SAVE, error);
    }
}
