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

public class SetUpGameCardsTest {
    private Rules rules;
    private EnemyRules enemyRules;

    @Before
    public void setUp() {
        rules = new Rules();
        enemyRules = new EnemyRules();
    }

    @Test
    public void setType_getCorrectType() {
        List<Card> chosenCards = new ArrayList<>();
        int numCards = 3;
        Card card1 = new Card("Apollo", true, "Apollo", rules, enemyRules);
        Card card2 = new Card("Artemis", true, "Artemis", rules, enemyRules);
        Card card3 = new Card("Athena", true, "Athena", rules, enemyRules);
        chosenCards.add(card1);
        chosenCards.add(card2);
        chosenCards.add(card3);
        SetUpGameCards setUpGameCards1 = new SetUpGameCards(numCards);
        SetUpGameCards setUpGameCards2 = new SetUpGameCards(chosenCards);
        assertEquals(CV, setUpGameCards1.getType());
        assertEquals(VC, setUpGameCards2.getType());
    }
}
