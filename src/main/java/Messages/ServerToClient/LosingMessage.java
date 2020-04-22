package Messages.ServerToClient;

import Client.View;
import Messages.MVMessage;
import Util.MessageType;

import java.io.Serializable;
//TODO eliminare

/**
 * Message to notify the loser
 */
public class LosingMessage implements Serializable, MVMessage {
    private final MessageType messageType;

    /**
     * Server-side constructor: build a request message
     */
    public LosingMessage() {
        messageType = MessageType.MV;
    }

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    public void execute(View view) {
        view.showLosingMessage();
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
