package share.network.requests;


import share.commands.CommandType;
import share.model.Dragon;

public class AddRequest extends Request {
    public final Dragon dragon;

    public AddRequest(Dragon dragon) {
        super(CommandType.ADD);
        this.dragon = dragon;
    }
}
