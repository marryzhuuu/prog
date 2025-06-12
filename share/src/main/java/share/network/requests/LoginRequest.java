package share.network.requests;

import share.commands.CommandType;
import share.model.User;

public class LoginRequest extends Request {
    public LoginRequest(User user) {
        super(CommandType.LOGIN, user);
    }

    @Override
    public boolean isAuth() {
        return true;
    }
}
