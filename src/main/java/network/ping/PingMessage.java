package network.ping;

import client.View;
import network.CVMessage;
import network.VCMessage;
import server.VirtualView;
import util.MessageType;

import java.io.Serializable;

/**
 * A ping message to keep the connection active
 */
public class PingMessage implements VCMessage, CVMessage, Serializable {
    private final MessageType messageType;

    /**
     * Constructor: build a PingMessage
     *
     * @param fromServer True if the message is from the server
     */
    public PingMessage(boolean fromServer) {
        if (fromServer) {
            messageType = MessageType.CV;
        } else {
            messageType = MessageType.VC;
        }
    }

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    public void execute(View view) {
    }

    /**
     * Execute the request server-side
     *
     * @param virtualView The recipient component
     */
    public void execute(VirtualView virtualView) {
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
