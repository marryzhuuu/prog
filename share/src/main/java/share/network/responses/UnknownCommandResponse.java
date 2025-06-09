package share.network.responses;

public class UnknownCommandResponse extends Response {
    public UnknownCommandResponse(String name) {
        super(name, "Unknown command");
    }
}
