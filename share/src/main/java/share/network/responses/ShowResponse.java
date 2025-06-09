package share.network.responses;

import share.commands.CommandType;
import share.model.Dragon;

import java.util.Vector;

public class ShowResponse extends Response {
    public final Vector<Dragon> dragons;

    public ShowResponse(Vector<Dragon> dragons, String error) {
        super(CommandType.SHOW, error);
        this.dragons = dragons;
    }
}
