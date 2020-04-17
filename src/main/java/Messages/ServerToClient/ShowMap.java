package Messages.ServerToClient;

import Client.View;
import Messages.CVMessage;
import Model.Game;
import Util.Frmt;
import Util.MessageType;

import java.io.Serializable;

public class ShowMap implements CVMessage, Serializable {
    private final MessageType messageType;
    private final Game game;
    private final String currentNickname;

    /**
     * Server-side constructor: build a request message
     *
     */
    public ShowMap(Game game, String currentNickname) {
        messageType = MessageType.CV;
        this.currentNickname = currentNickname;
        this.game=game;
    }

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    public void execute(View view) {
        view.showMap(game);
        view.showMessage("\n\n\t"+ Frmt.style('b',"It's "+currentNickname.toUpperCase()+" turn."));
    }

    /**
     * Gets the message type
     *
     * @return The message type
     */
    public MessageType getType() {
        return messageType;
    }
}