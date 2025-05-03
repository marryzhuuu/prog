package share.network.requests;


import share.commands.CommandType;

public class CountGreaterThanAgeRequest extends Request {
    public final int age;

    public CountGreaterThanAgeRequest(int age) {
        super(CommandType.COUNT_GREATER_THAN_AGE);
        this.age = age;
    }
}
