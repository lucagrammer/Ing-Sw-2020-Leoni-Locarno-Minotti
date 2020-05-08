package model;

import server.rules.EnemyRules;
import server.rules.Rules;

import java.io.Serializable;
import java.util.Objects;

/**
 * Stores information about a card of the game
 */
public class Card implements Serializable {

    private final String name;
    private final boolean threePlayersFlag;
    private final String description;
    private final Rules rules;
    private final EnemyRules enemyRules;

    /**
     * Constructor: build the Card
     *
     * @param name             The name of the card
     * @param threePlayersFlag Three player compatibility of the card
     * @param description      Description of the card effect
     * @param rules            The game rules for the player that owns this card
     * @param enemyRules       The game rules for the enemies
     */
    public Card(String name, boolean threePlayersFlag, String description, Rules rules, EnemyRules enemyRules) {
        this.name = name;
        this.threePlayersFlag = threePlayersFlag;
        this.description = description;
        this.rules = rules;
        this.enemyRules = enemyRules;
    }

    /**
     * Gets the name of the card
     *
     * @return The name of the card
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the card
     *
     * @return The description of the card
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks if the card can be used in a three player game
     *
     * @return True if the card can be used in a three player game, otherwise false
     */
    public boolean isThreePlayersCompatible() {
        return threePlayersFlag;
    }

    /**
     * Gets the card rules for the player that own this card
     *
     * @return The card rules fot the player that own this card
     */
    public Rules getRules() {
        return rules;
    }

    /**
     * Gets the card rules for the enemies
     *
     * @return The card rules fot the enemies
     */
    public EnemyRules getEnemyRules() {
        return enemyRules;
    }

    /**
     * Tests if the current object is equal to another object
     *
     * @param obj The other object
     * @return True if the objects are the same
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return Objects.equals(name, card.name);
    }

}