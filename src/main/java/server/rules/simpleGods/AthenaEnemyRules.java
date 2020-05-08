package server.rules.simpleGods;

import model.Game;
import model.Player;
import server.rules.EnemyRules;
import util.Action;
import util.ActionType;
import util.RoundActions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Athena Card
 */
public class AthenaEnemyRules extends EnemyRules {

    /**
     * Fixes the enemy actions according to the enemy rules
     *
     * @param enemyPossibleActions The enemy actions to be fixed
     * @param game                 The game
     * @param player               The owner of the card that fixed the actions
     * @return The fixed enemy actions
     */
    public RoundActions fixEnemyActions(RoundActions enemyPossibleActions, Game game, Player player) {
        RoundActions myActions = player.getRoundActions();
        RoundActions fixedActions = new RoundActions();

        if (myActions.hasMovedUp()) {
            List<Action> allowedActionList = enemyPossibleActions.
                    getActionList().
                    stream().
                    filter(x -> x.getActionType() != ActionType.MOVE || x.getLevelDifference() <= 0).
                    collect(Collectors.toList());
            fixedActions.add(allowedActionList);
            if (fixedActions.isEmpty())
                fixedActions.add(new Action(ActionType.LOSE));
        } else {
            fixedActions.add(enemyPossibleActions.getActionList());
        }
        return fixedActions;
    }
}
