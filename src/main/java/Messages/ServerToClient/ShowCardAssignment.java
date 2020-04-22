package Messages.ServerToClient;

import Client.View;
import Messages.MVMessage;
import Model.Player;
import Util.MessageType;

import java.io.Serializable;
import java.util.List;

/**
 * Message to inform the player of the allocation of cards
 */
public class ShowCardAssignment implements Serializable, MVMessage {
    private final MessageType messageType;
    private final List<Player> playerList;

    /**
     * Server-side constructor: build a message
     *
     * @param playerList The list of players
     */
    public ShowCardAssignment(List<Player> playerList) {
        messageType = MessageType.MV;
        this.playerList = playerList;
    }

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    public void execute(View view) {
        view.showCardAssignmentMessage(playerList);
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