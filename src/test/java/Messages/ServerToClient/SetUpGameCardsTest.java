package Messages.ServerToClient;

import model.Card;
import Server.Rules.EnemyRules;
import Server.Rules.Rules;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static Util.MessageType.CV;
import static Util.MessageType.VC;
import static org.junit.Assert.assertEquals;

public class SetUpGameCardsTest {
    static SetUpGameCards setUpGameCards1;
    static SetUpGameCards setUpGameCards2;
    Rules rules;
    EnemyRules enemyRules;

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
        setUpGameCards1 = new SetUpGameCards(numCards);
        setUpGameCards2 = new SetUpGameCards(chosenCards);
        assertEquals(CV, setUpGameCards1.getType());
        assertEquals(VC, setUpGameCards2.getType());
    }
}
