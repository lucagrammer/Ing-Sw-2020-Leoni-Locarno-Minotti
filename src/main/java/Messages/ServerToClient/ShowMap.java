package Messages.ServerToClient;

import Client.View;
import Messages.CVMessage;
import Model.Game;
import Util.Frmt;
import Util.MessageType;

import java.io.Serializable;

/**
 * Message to show the updated game map to the user
 */
public class ShowMap implements CVMessage, Serializable {
    private final MessageType messageType;
    private final Game game;
    private final String currentNickname;
    private final String loserNickname;

    /**
     * Server-side constructor: build a request message
     *
     * @param currentNickname The nickname of the current player
     * @param game            The game
     * @param loserNickname   The nickname of the loser player or null value
     */
    public ShowMap(Game game, String currentNickname, String loserNickname) {
        messageType = MessageType.CV;
        this.currentNickname = currentNickname;
        this.loserNickname = loserNickname;
        this.game = game;
    }

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    public void execute(View view) {
        view.showMap(game, true);
        if (loserNickname != null) {
            view.showMessage("\n\n\t\t" + Frmt.style('b', Frmt.color('r', Frmt.DEATH + " " + loserNickname.toUpperCase() + " has lost")), false);
        }
        view.showMessage(((loserNickname == null) ? "\n\n\t\t" : "\t\t") + Frmt.style('b', "It's " + currentNickname.toUpperCase() + " turn."), false);
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