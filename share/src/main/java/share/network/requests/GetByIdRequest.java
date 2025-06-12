package share.network.requests;


import share.commands.CommandType;
import share.model.User;

public class GetByIdRequest extends Request {
    public final int id;

    public GetByIdRequest(int id, User user) {
        super(CommandType.GET, user);
        this.id = id;
    }
}
