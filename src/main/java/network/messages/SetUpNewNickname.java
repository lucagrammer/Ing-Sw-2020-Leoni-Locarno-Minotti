package network.messages;

import client.View;
import network.CVMessage;
import network.VCMessage;
import server.VirtualView;
import util.MessageType;

import java.io.Serializable;

/**
 * Message requesting to reset the nickname because it is already in use. Response message to the reset request.
 */
public class SetUpNewNickname implements CVMessage, VCMessage, Serializable {
    private final MessageType messageType;
    private final String temporaryNickname;
    private String nickname;

    /**
     * Client-side constructor: build a response message
     *
     * @param nickname          The new chosen nickname
     * @param temporaryNickname The temporary nickname of the user
     */
    public SetUpNewNickname(String temporaryNickname, String nickname) {
        this.messageType = MessageType.VC;
        this.nickname = nickname;
        this.temporaryNickname = temporaryNickname;
    }


    /**
     * Server-side constructor: build a request message
     *
     * @param temporaryNickname The temporary nickname of the client
     */
    public SetUpNewNickname(String temporaryNickname) {
        this.messageType = MessageType.CV;
        this.temporaryNickname = temporaryNickname;
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
     * Gets the temporary nickname of the user
     *
     * @return The temporary nickname
     */
    public String getTemporaryNickname() {
        return temporaryNickname;
    }

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    public void execute(View view) {
        view.askNewNickname();
    }

    /**
     * Execute the request server-side
     *
     * @param virtualView The recipient component
     */
    public void execute(VirtualView virtualView) {
        virtualView.setNewNickname(temporaryNickname, nickname);
    }
}
