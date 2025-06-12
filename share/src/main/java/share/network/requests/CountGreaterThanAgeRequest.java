package share.network.requests;


import share.commands.CommandType;
import share.model.User;

public class CountGreaterThanAgeRequest extends Request {
    public final int age;

    public CountGreaterThanAgeRequest(int age, User user) {
        super(CommandType.COUNT_GREATER_THAN_AGE, user);
        this.age = age;
    }
}
