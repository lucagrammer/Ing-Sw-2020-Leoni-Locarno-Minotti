package Controller.Rules;

import Model.Game;
import Model.Player;
import Util.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class EnemyRulesTest {
    static EnemyRules enemyRules;
    static Game game;
    static Player player1;

    @Before
    public void setUp() {
        player1 = new Player("AlphaTester", new Date(System.currentTimeMillis()));
        enemyRules = new EnemyRules();
    }

    @Test
    public void notAllowedEnemyActions_noChange() {
        RoundActions unfixedActions = new RoundActions();
        unfixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.SW, 1));
        unfixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 0));
        unfixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, -1));
        unfixedActions.add(new Action(ActionType.BUILD_FLOOR, Genre.MALE, Direction.SW, 1));
        unfixedActions.add(new Action(ActionType.BUILD_DOME, Genre.MALE, Direction.S, 0));
        unfixedActions.add(new Action(ActionType.BUILD_FLOOR, Genre.MALE, Direction.S, -1));
        unfixedActions.add(new Action(ActionType.END));
        RoundActions fixedActions = enemyRules.fixEnemyActions(unfixedActions, game, player1);
        assertEquals(unfixedActions, fixedActions);
    }
}