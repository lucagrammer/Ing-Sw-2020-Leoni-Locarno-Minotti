package Messages.ServerToClient;

import Client.View;
import Messages.MVMessage;
import Model.Card;
import Util.MessageType;

import java.io.Serializable;
import java.util.List;

/**
 * Message to inform users of the game cards selected by the challenger
 */
public class CardInfo implements Serializable, MVMessage {
    private final MessageType messageType;
    private final List<Card> cards;

    /**
     * Server-side constructor: build the message
     *
     * @param cards The game cards
     */
    public CardInfo(List<Card> cards) {
        messageType = MessageType.MV;
        this.cards = cards;
    }

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    public void execute(View view) {
        view.showGameCards(cards);
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

