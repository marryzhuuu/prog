package share.network.responses;

import share.commands.CommandType;
import share.model.User;

public class RegisterResponse extends Response {
    public final User user;

    public RegisterResponse(User user, String error) {
        super(CommandType.REGISTER, error);
        this.user = user;
    }
}
