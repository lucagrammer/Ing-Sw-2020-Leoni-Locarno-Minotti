package Messages.ServerToClient;

import Model.Card;
import Server.Rules.EnemyRules;
import Server.Rules.Rules;
import Util.MessageType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static Util.MessageType.*;
import static org.junit.Assert.*;

public class ChooseCardsTest {
    static ChooseCards chooseCards1;
    static ChooseCards chooseCards2;
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
        chooseCards1 = new ChooseCards(numCards);
        chooseCards2 = new ChooseCards(chosenCards);
        assertEquals(CV, chooseCards1.getType());
        assertEquals(VC, chooseCards2.getType());
    }
}
