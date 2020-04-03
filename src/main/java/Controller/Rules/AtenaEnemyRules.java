package Controller.Rules;

import Model.Game;
import Model.Player;
import Util.Action;
import Util.ActionType;
import Util.RoundActions;

import java.util.List;
import java.util.stream.Collectors;

public class AtenaEnemyRules extends EnemyRules {

    @Override
    RoundActions fixEnemyActions(RoundActions enemyPossibleActions, Game game, Player player) {
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
