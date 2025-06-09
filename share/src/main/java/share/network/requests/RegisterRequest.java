package share.network.requests;


import share.commands.CommandType;
import share.model.User;

public class RegisterRequest extends Request {
    public final User user;

    public RegisterRequest(User user) {
        super(CommandType.REGISTER);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
