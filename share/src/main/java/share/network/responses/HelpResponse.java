package share.network.responses;

public class HelpResponse extends Response {
    public final String helpMessage;

    public HelpResponse(String helpMessage, String error) {
        super("help", error);
        this.helpMessage = helpMessage;
    }
}
