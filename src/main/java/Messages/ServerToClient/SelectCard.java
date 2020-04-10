package Messages.ServerToClient;

import Client.View;
import Messages.CVMessage;
import Messages.VCMessage;
import Model.Card;
import Server.VirtualView;
import Util.MessageType;

import java.io.Serializable;
import java.util.List;

public class SelectCard implements Serializable, CVMessage, VCMessage {
    private MessageType messageType;
    private List<Card> possibleChoices;
    private Card choice;
    private String nickname;

    public SelectCard(List<Card> possibleChoices) {
        messageType = MessageType.CV;
        this.possibleChoices = possibleChoices;
    }

    public SelectCard(Card choice, String nickname) {
        messageType = MessageType.VC;
        this.choice = choice;
        this.nickname = nickname;
    }

    public MessageType getType() {
        return messageType;
    }

    public void execute(View view) {
        view.chooseCard(possibleChoices);
    }

    public void execute(VirtualView virtualView) {
        virtualView.setCard(choice, nickname);
    }
}
