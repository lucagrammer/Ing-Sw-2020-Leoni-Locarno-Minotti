package network;

import util.MessageType;

/**
 * Message between client and server
 */
public interface Message {

    /**
     * Gets the message type
     *
     * @return The message type
     */
    MessageType getType();
}
