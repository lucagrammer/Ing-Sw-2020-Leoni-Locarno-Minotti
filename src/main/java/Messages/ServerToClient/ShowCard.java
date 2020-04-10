package Messages.ServerToClient;

import Client.View;
import Messages.CVMessage;
import Model.Card;
import Util.MessageType;

import java.io.Serializable;

public class ShowCard implements Serializable, CVMessage {
    private MessageType messageType;
    private Card card;

    public ShowCard(Card card) {
        messageType = MessageType.CV;
        this.card = card;
    }

    public void execute(View view) {
        view.showMessage("\nYour card is:\n\t" + card.getName() + "\n");
    }

    public MessageType getType() {
        return messageType;
    }
}
