package Messages.ServerToClient;

import Model.Card;
import Server.Rules.EnemyRules;
import Server.Rules.Rules;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static Util.MessageType.MV;
import static org.junit.Assert.*;

public class CardInfoTest {
    static CardInfo cardInfo;
    Rules rules;
    EnemyRules enemyRules;

    @Test
    public void setType_getCorrectType() {
        List<Card> cards = new ArrayList<>();
        Card card1 = new Card("Apollo", true, "Apollo", rules, enemyRules);
        Card card2 = new Card("Artemis", true, "Artemis", rules, enemyRules);
        Card card3 = new Card("Athena", true, "Athena", rules, enemyRules);
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        cardInfo= new CardInfo(cards);
        assertEquals(MV, cardInfo.getType());
    }
}