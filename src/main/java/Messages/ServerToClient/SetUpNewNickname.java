package Messages.ServerToClient;

import Client.View;
import Messages.CVMessage;
import Util.MessageType;

import java.io.Serializable;

/**
 * Message requesting to reset the nickname because it is already in use. Response message to the reset request.
 */
public class SetUpNewNickname implements CVMessage, Serializable {
    private final MessageType messageType;
    private String nickname;

    /**
     * Client-side constructor: build a response message
     *
     * @param nickname The new chosen nickname
     */
    public SetUpNewNickname(String nickname) {
        this.messageType = MessageType.CV;
        this.nickname = nickname;
    }

    /**
     * Server-side constructor: build a request message
     */
    public SetUpNewNickname() {
        this.messageType = MessageType.CV;
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
     * Gets the new chosen nickname
     *
     * @return The new chosen nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    public void execute(View view) {
        view.askNewNickname();
    }
}
