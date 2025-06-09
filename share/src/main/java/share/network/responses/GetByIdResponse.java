package share.network.responses;

import share.commands.CommandType;
import share.model.Dragon;

public class GetByIdResponse extends Response {
    public final Dragon dragon;

    public GetByIdResponse(Dragon dragon, String error) {
        super(CommandType.GET, error);
        this.dragon = dragon;
    }
}
