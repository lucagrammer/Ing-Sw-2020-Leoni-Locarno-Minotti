package Messages.ServerToClient;

import Client.View;
import Messages.MVMessage;
import Util.MessageType;

import java.io.Serializable;

/**
 * Message to notify the player that another player has disconnected. Notify the end of the game.
 */
public class NotifyDisconnection implements MVMessage, Serializable {
    private final MessageType messageType;
    private final String disconnectedNickname;

    /**
     * Server-side constructor: build the message
     *
     * @param disconnectedNickname The nickname of the disconnected user
     */
    public NotifyDisconnection(String disconnectedNickname) {
        messageType = MessageType.MV;
        this.disconnectedNickname = disconnectedNickname;
    }

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    public void execute(View view) {
        view.showDisconnection(disconnectedNickname);

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