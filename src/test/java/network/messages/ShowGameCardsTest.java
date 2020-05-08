package network.messages;

import model.Card;
import org.junit.Before;
import org.junit.Test;
import server.rules.EnemyRules;
import server.rules.Rules;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static util.MessageType.MV;

public class ShowGameCardsTest {
    private List<Card> cards;

    @Before
    public void setUp() {
        Rules rules = new Rules();
        EnemyRules enemyRules = new EnemyRules();
        cards = new ArrayList<>();
        Card card1 = new Card("Apollo", true, "Apollo", rules, enemyRules);
        Card card2 = new Card("Artemis", true, "Artemis", rules, enemyRules);
        Card card3 = new Card("Athena", true, "Athena", rules, enemyRules);
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
    }

    @Test
    public void setType_getCorrectType() {
        ShowGameCards showGameCards = new ShowGameCards(cards);
        assertEquals(MV, showGameCards.getType());
    }
}