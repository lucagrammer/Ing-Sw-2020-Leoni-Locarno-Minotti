package Messages.ServerToClient;

import Client.View;
import Messages.CVMessage;
import Messages.VCMessage;
import Server.VirtualView;
import Util.MessageType;

import java.io.Serializable;
import java.util.List;

/**
 * Message for the choice of the color of the player
 */
public class SetUpPlayerColor implements CVMessage, VCMessage, Serializable {
    final MessageType messageType;
    List<String> availableColors;
    String color;
    String nickname;

    /**
     * Server-side constructor: build a request message
     *
     * @param availableColors The available colors
     */
    public SetUpPlayerColor(List<String> availableColors) {
        this.messageType = MessageType.CV;
        this.availableColors = availableColors;
    }

    /**
     * Client-side constructor: build a response message
     *
     * @param color    The chosen color
     * @param nickname The nickname of the player
     */
    public SetUpPlayerColor(String color, String nickname) {
        this.messageType = MessageType.VC;
        this.color = color;
        this.nickname = nickname;
    }

    /**
     * Gets the message type
     *
     * @return The message type
     */
    public MessageType getType() {
        return messageType;
    }

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    public void execute(View view) {
        view.askPlayerColor(availableColors);
    }

    /**
     * Execute the request server-side
     *
     * @param virtualView The recipient component
     */
    public void execute(VirtualView virtualView) {
        virtualView.setPlayerColor(nickname, color);
    }
}
