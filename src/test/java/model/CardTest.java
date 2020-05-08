package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.rules.EnemyRules;
import server.rules.Rules;

import static org.junit.Assert.*;

public class CardTest {

    private Card card;
    private String name;
    private String description;
    private Rules rules;
    private EnemyRules enemyRules;

    @Before
    public void setUp() {
        name = "Name card";
        description = "description";
    }

    @After
    public void tearDown() {
        name = null;
        description = null;
    }

    @Test
    public void setName_getCorrectName() {
        card = new Card("Apollo", true, description, rules, enemyRules);
        assertEquals("Apollo", card.getName());
    }

    @Test
    public void setDescription_getCorrectDescription() {
        card = new Card(name, false, "description", rules, enemyRules);
        assertEquals("description", card.getDescription());
    }

    @Test
    public void setCompatibility_getCorrectCompatibility() {
        Card card1;
        Card card2;
        card1 = new Card(name, true, description, rules, enemyRules);
        card2 = new Card(name, false, description, rules, enemyRules);
        assertTrue(card1.isThreePlayersCompatible());
        assertFalse(card2.isThreePlayersCompatible());
    }

    @Test
    public void getCorrectRules() {
        rules = new Rules();
        card = new Card(name, true, description, rules, enemyRules);
        assertEquals(rules, card.getRules());
    }

    @Test
    public void getCorrectEnemyRules() {
        enemyRules = new EnemyRules();
        card = new Card(name, false, description, rules, enemyRules);
        assertEquals(enemyRules, card.getEnemyRules());
    }

    @Test
    public void testEquals() {
        Card card1;
        Card card2;
        Card card3;
        card1 = new Card("Apollo", true, description, rules, enemyRules);
        card2 = new Card("Apollo", true, description, rules, enemyRules);
        card3 = new Card("Artemis", true, description, rules, enemyRules);
        assertEquals(card1, card2);
        assertNotEquals(card1, card3);
    }
}
