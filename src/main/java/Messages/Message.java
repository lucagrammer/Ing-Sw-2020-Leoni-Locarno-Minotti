package Messages;

import Util.MessageType;

/**
 * Message between client and server
 */
public interface Message {
    MessageType getType();
}
