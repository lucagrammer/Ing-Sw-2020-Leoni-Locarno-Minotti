package Server.Rules;

import Model.Game;
import Model.Player;
import Util.RoundActions;

import java.io.Serializable;

/**
 * The regular enemy rules
 */
public class EnemyRules implements Serializable {

    /**
     * Fixes the enemy actions according to the enemy rules
     *
     * @param enemyPossibleActions The enemy actions to be fixed
     * @param game                 The game
     * @param player               The owner of the card that fixed the actions
     * @return The fixed enemy actions
     */
    public RoundActions fixEnemyActions(RoundActions enemyPossibleActions, Game game, Player player) {
        RoundActions fixedActions = new RoundActions();
        fixedActions.add(enemyPossibleActions);
        return fixedActions;
    }
}
