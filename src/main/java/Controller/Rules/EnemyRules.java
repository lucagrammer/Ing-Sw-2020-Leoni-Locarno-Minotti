package Controller.Rules;

import Model.Game;
import Model.Player;
import Util.RoundActions;

public class EnemyRules {

    public RoundActions fixEnemyActions(RoundActions enemyPossibleActions, Game game, Player player) {
        RoundActions fixedActions = new RoundActions();
        fixedActions.add(enemyPossibleActions);
        return fixedActions;
    }
}
