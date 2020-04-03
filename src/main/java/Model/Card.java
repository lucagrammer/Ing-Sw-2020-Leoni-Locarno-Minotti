package Model;

import Controller.Rules.*;

public class Card {

    private String name;
    private boolean threePlayersCompatibility;
    private String description;
    private Rules rules;
    private EnemyRules enemyRules;

    /**
     * Build the Card
     *
     * @param name                      the name of the card
     * @param threePlayersCompatibility three player compatibility of the card
     * @param description               description of the card effect
     * @param rules                     the game rules for the player that owns this card
     * @param enemyRules                the game rules for the enemies
     */
    public Card(String name, boolean threePlayersCompatibility, String description, Rules rules, EnemyRules enemyRules) {
        this.name = name;
        this.threePlayersCompatibility = threePlayersCompatibility;
        this.description = description;
        this.rules = rules;
        this.enemyRules = enemyRules;
    }

    /**
     * Gets the name of the card
     *
     * @return the name of the card
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the card
     *
     * @return the description of the card
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks if the card can be used in a three player game
     *
     * @return true if the card can be used in a three player game, otherwise false
     */
    public boolean isThreePlayersCompatible() {
        return threePlayersCompatibility;
    }

    /**
     * Gets the card rules for the player that own this card
     *
     * @return the card rules fot the player that own this card
     */
    public Rules getRules() {
        return rules;
    }

    /**
     * Gets the card rules for the enemies
     *
     * @return the card rules fot the enemies
     */
    public EnemyRules getEnemyRules() {
        return enemyRules;
    }
}