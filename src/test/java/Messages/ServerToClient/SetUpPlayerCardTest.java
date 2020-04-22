package Messages.ServerToClient;

import Model.Card;
import org.junit.Test;

import java.util.List;

import static Util.MessageType.CV;
import static Util.MessageType.VC;
import static org.junit.Assert.assertEquals;

public class SetUpPlayerCardTest {
    SetUpPlayerCard setUpPlayerCard1, setUpPlayerCard2;
    List<Card> possibleChoices;
    Card choice;
    String nickname;

    @Test
    public void setType_getCorrectType() {
        setUpPlayerCard1 = new SetUpPlayerCard(possibleChoices);
        setUpPlayerCard2 = new SetUpPlayerCard(choice, nickname);
        assertEquals(CV, setUpPlayerCard1.getType());
        assertEquals(VC, setUpPlayerCard2.getType());
    }
}