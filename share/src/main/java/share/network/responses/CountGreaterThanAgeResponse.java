package share.network.responses;

import share.commands.CommandType;

public class CountGreaterThanAgeResponse extends Response {
    public final long count;

    public CountGreaterThanAgeResponse(long count, String error)
    {
        super(CommandType.COUNT_GREATER_THAN_AGE, error);
        this.count = count;
    }
}
