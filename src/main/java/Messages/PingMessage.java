package Messages;

import Client.View;
import Server.VirtualView;
import Util.MessageType;

import java.io.Serializable;

public class PingMessage implements VCMessage, CVMessage, Serializable {
    private final MessageType messageType;

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
