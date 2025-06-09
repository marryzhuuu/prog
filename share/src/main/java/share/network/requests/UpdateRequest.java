package share.network.requests;


import share.commands.CommandType;
import share.model.Dragon;

public class UpdateRequest extends Request {
    public final Dragon dragon;
    public final int id;

    public UpdateRequest(int id, Dragon dragon) {
        super(CommandType.UPDATE);
        this.dragon = dragon;
        this.id = id;
    }
}
