package share.network.responses;

import share.model.Dragon;

import java.util.List;

public class ShowResponse extends Response {
    public final List<Dragon> dragons;

    public ShowResponse(List<Dragon> dragons, String error) {
        super("show", error);
        this.dragons = dragons;
    }
}
