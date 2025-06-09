package share.network.requests;


import share.commands.CommandType;

public class GetByIdRequest extends Request {
    public final int id;

    public GetByIdRequest(int id) {
        super(CommandType.GET);
        this.id = id;
    }
}
