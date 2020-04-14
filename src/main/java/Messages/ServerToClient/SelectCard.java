package Messages.ServerToClient;

import Client.View;
import Messages.CVMessage;
import Messages.VCMessage;
import Model.Card;
import Server.VirtualView;
import Util.MessageType;

import java.io.Serializable;
import java.util.List;

/**
 * Message to manage the choice of the player's card
 */
public class SelectCard implements Serializable, CVMessage, VCMessage {
    private final MessageType messageType;
    private List<Card> possibleChoices;
    private Card choice;
    private String nickname;

    /**
     * Server-side constructor: build a request message
     *
     * @param possibleChoices The possible cards
     */
    public SelectCard(List<Card> possibleChoices) {
        messageType = MessageType.CV;
        this.possibleChoices = possibleChoices;
    }

    /**
     * Client-side constructor: build a response message
     *
     * @param choice   The chosen card
     * @param nickname The nickname of the player
     */
    public SelectCard(Card choice, String nickname) {
        messageType = MessageType.VC;
        this.choice = choice;
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
        view.chooseCard(possibleChoices);
    }

    /**
     * Execute the request server-side
     *
     * @param virtualView The recipient component
     */
    public void execute(VirtualView virtualView) {
        virtualView.setCard(choice, nickname);
    }
}
