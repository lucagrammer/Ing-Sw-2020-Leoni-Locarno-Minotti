package server.rules;

import model.Game;
import model.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.*;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class EnemyRulesTest {
    private static EnemyRules enemyRules;
    private static Game game;
    private static Player player1;

    @Before
    public void setUp() {
        player1 = new Player("AlphaTester", new Date(System.currentTimeMillis()), false);
        enemyRules = new EnemyRules();
    }

    @After
    public void tearDown() {
        enemyRules = null;
        game = null;
        player1 = null;
    }

    @Test
    public void notAllowedEnemyActions_noChange() {
        RoundActions unfixedActions = new RoundActions();
        unfixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.SW, 1));
        unfixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 0));
        unfixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, -1));
        unfixedActions.add(new Action(ActionType.FLOOR, Genre.MALE, Direction.SW, 1));
        unfixedActions.add(new Action(ActionType.DOME, Genre.MALE, Direction.S, 0));
        unfixedActions.add(new Action(ActionType.FLOOR, Genre.MALE, Direction.S, -1));
        unfixedActions.add(new Action(ActionType.END));
        RoundActions fixedActions = enemyRules.fixEnemyActions(unfixedActions, game, player1);
        assertEquals(unfixedActions, fixedActions);
    }
}