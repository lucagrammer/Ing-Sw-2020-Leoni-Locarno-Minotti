package network.messages;

import client.View;
import network.CVMessage;
import network.VCMessage;
import server.VirtualView;
import util.Action;
import util.MapInfo;
import util.MessageType;
import util.RoundActions;

import java.io.Serializable;

/**
 * Message to ask an action to be performed in the turn
 */
public class Turn implements Serializable, CVMessage, VCMessage {
    private final MessageType messageType;
    private RoundActions roundActions;
    private MapInfo mapInfo;
    private Action action;
    private String nickname;
    private String loserNickname;

    /**
     * Server-side constructor: build a request message
     *
     * @param roundActions  The possible actions
     * @param mapInfo       The map info
     * @param loserNickname The nickname of the loser or null value
     */
    public Turn(RoundActions roundActions, MapInfo mapInfo, String loserNickname) {
        messageType = MessageType.CV;
        this.roundActions = roundActions;
        this.mapInfo = mapInfo;
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
        view.askAction(roundActions, mapInfo, loserNickname);
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