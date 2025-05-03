package share.network.requests;


import share.commands.CommandType;
import share.model.Dragon;

public class AddIfMaxRequest extends Request {
    public final Dragon dragon;

    public AddIfMaxRequest(Dragon dragon) {
        super(CommandType.ADD_IF_MAX);
        this.dragon = dragon;
    }
}
