package share.network.responses;

import share.commands.CommandType;

public class AddResponse extends Response {

    public AddResponse(String error) {
        super(CommandType.ADD, error);
    }
}
