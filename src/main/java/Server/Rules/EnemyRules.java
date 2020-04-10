package Server.Rules;

import Model.Game;
import Model.Player;
import Util.RoundActions;

import java.io.Serializable;

public class EnemyRules implements Serializable {

    public RoundActions fixEnemyActions(RoundActions enemyPossibleActions, Game game, Player player) {
        RoundActions fixedActions = new RoundActions();
        fixedActions.add(enemyPossibleActions);
        return fixedActions;
    }
}
