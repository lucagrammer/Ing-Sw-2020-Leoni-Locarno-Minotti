package Messages.ServerToClient;

import Client.View;
import Messages.CVMessage;
import Messages.VCMessage;
import Model.Card;
import Server.VirtualView;
import Util.MessageType;

import java.io.Serializable;
import java.util.List;

public class ChooseCards implements CVMessage, VCMessage, Serializable {
    MessageType messageType;
    int numCards;
    List<Card> chosenCards;

    public ChooseCards(int numCards) {
        messageType = MessageType.CV;
        this.numCards = numCards;
    }

    public ChooseCards(List<Card> chosenCards) {
        messageType = MessageType.VC;
        this.chosenCards = chosenCards;
    }

    public MessageType getType() {
        return messageType;
    }

    public void execute(View view) {
        view.chooseCards(numCards);
    }

    public void execute(VirtualView virtualView) {
        virtualView.hasChosenCards(chosenCards);
    }
}
