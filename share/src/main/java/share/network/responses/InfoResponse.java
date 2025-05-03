package share.network.responses;

public class InfoResponse extends Response {
    public final String infoMessage;

    public InfoResponse(String infoMessage, String error) {
        super("info", error);
        this.infoMessage = infoMessage;
    }
}
