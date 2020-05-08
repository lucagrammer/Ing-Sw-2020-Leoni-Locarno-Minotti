package network.messages;

import model.Card;
import org.junit.Before;
import org.junit.Test;
import server.rules.EnemyRules;
import server.rules.Rules;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static util.MessageType.CV;
import static util.MessageType.VC;

public class SetUpPlayerCardTest {
    private List<Card> possibleChoices;
    private Card choice;
    private String nickname;

    @Before
    public void setUp() {
        Card c1 = new Card("C1", true, "This is c1", new Rules(), new EnemyRules());
        Card c2 = new Card("C2", true, "This is c2", new Rules(), new EnemyRules());

        possibleChoices = new ArrayList<>();
        possibleChoices.add(c1);
        possibleChoices.add(c2);
        choice = c1;
        nickname = "Joe";
    }

    @Test
    public void setType_getCorrectType() {
        SetUpPlayerCard setUpPlayerCard1 = new SetUpPlayerCard(possibleChoices);
        SetUpPlayerCard setUpPlayerCard2 = new SetUpPlayerCard(choice, nickname);
        assertEquals(CV, setUpPlayerCard1.getType());
        assertEquals(VC, setUpPlayerCard2.getType());
    }
}