package share.network.responses;

import share.commands.CommandType;
import share.model.User;

public class LoginResponse extends Response {
    public final User user;

    public LoginResponse(User user, String error) {
        super(CommandType.LOGIN, error);
        this.user = user;
    }
}
