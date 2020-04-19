package Messages.ServerToClient;

import Model.Card;
import org.junit.Test;

import java.util.List;

import static Util.MessageType.CV;
import static Util.MessageType.VC;
import static org.junit.Assert.*;

public class SelectCardTest {
    SelectCard selectCard1, selectCard2;
    List<Card> possibleChoices;
    Card choice;
    String nickname;

    @Test
    public void setType_getCorrectType() {
        selectCard1= new SelectCard(possibleChoices);
        selectCard2= new SelectCard(choice, nickname);
        assertEquals(CV, selectCard1.getType());
        assertEquals(VC, selectCard2.getType());
    }
}