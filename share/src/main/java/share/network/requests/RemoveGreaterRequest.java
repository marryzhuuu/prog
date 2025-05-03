package share.network.requests;


import share.commands.CommandType;
import share.model.Dragon;

public class RemoveGreaterRequest extends Request {
    public final Dragon dragon;

    public RemoveGreaterRequest(Dragon dragon) {
        super(CommandType.REMOVE_GREATER);
        this.dragon = dragon;
    }
}
