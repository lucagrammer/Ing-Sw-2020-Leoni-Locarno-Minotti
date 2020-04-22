package Messages.ServerToClient;

import Client.View;
import Messages.CVMessage;
import Messages.VCMessage;
import Model.Game;
import Server.VirtualView;
import Util.Action;
import Util.MessageType;
import Util.RoundActions;

import java.io.Serializable;

/**
 * Message to ask an action to be performed in the turn
 */
public class Turn implements Serializable, CVMessage, VCMessage {
    private final MessageType messageType;
    private RoundActions roundActions;
    private Game game;
    private Action action;
    private String nickname;
    private String loserNickname;

    /**
     * Server-side constructor: build a request message
     *
     * @param roundActions  The possible actions
     * @param game          The game
     * @param loserNickname The nickname of the looser or null value
     */
    public Turn(RoundActions roundActions, Game game, String loserNickname) {
        messageType = MessageType.CV;
        this.roundActions = roundActions;
        this.game = game;
        this.loserNickname = loserNickname;
    }

    /**
     * Client-side constructor: build a response message
     *
     * @param action   The chosen action
     * @param nickname The nickname of the player
     */
    public Turn(Action action, String nickname) {
        messageType = MessageType.VC;
        this.action = action;
        this.nickname = nickname;
    }

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    public void execute(View view) {
        view.askAction(roundActions, game, loserNickname);
    }

    /**
     * Execute the request server-side
     *
     * @param virtualView The recipient component
     */

    public void execute(VirtualView virtualView) {
        virtualView.setAction(action, nickname);
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