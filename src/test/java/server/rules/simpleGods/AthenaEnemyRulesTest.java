package server.rules.simpleGods;

import model.Board;
import model.Game;
import model.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.rules.EnemyRules;
import util.*;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class AthenaEnemyRulesTest {
    private static EnemyRules enemyRules;
    private static Game game;
    private static Board board;
    private static Player player1, player2;

    @Before
    public void setUp() {
        player1 = new Player("AlphaTester", new Date(System.currentTimeMillis()), false);
        player2 = new Player("BetaTester", new Date(System.currentTimeMillis()), false);
        game = new Game();
        game.setNumPlayers(2);
        game.addPlayer(player1);
        game.addPlayer(player2);
        board = game.getBoard();
        player1.chooseColor(PlayerColor.PURPLE);
        player2.chooseColor(PlayerColor.YELLOW);
        enemyRules = new AthenaEnemyRules();
    }

    @After
    public void tearDown() {
        enemyRules = null;
        game = null;
        board = null;
        player1 = null;
        player2 = null;
    }

    @Test
    public void notAllowedEnemyActions_multipleActionFixable_fixedActions() {
        RoundActions unfixedActions = new RoundActions();
        unfixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.SW, 1));
        unfixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.N, 1));
        unfixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 0));
        unfixedActions.add(new Action(ActionType.FLOOR, Genre.MALE, Direction.SW, 1));
        unfixedActions.add(new Action(ActionType.DOME, Genre.MALE, Direction.S, 0));
        unfixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, -1));
        unfixedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.W, 2));
        unfixedActions.add(new Action(ActionType.FLOOR, Genre.MALE, Direction.S, -1));
        unfixedActions.add(new Action(ActionType.END));

        player1.registerAction(new Action(ActionType.MOVE, Genre.FEMALE, Direction.SW, 1));
        player1.registerAction(new Action(ActionType.FLOOR, Genre.FEMALE, Direction.SW, 0));

        RoundActions fixedActions = enemyRules.fixEnemyActions(unfixedActions, game, player1);

        // Expected actions
        RoundActions expectedFixedActions = new RoundActions();
        expectedFixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 0));
        expectedFixedActions.add(new Action(ActionType.FLOOR, Genre.MALE, Direction.SW, 1));
        expectedFixedActions.add(new Action(ActionType.DOME, Genre.MALE, Direction.S, 0));
        expectedFixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, -1));
        expectedFixedActions.add(new Action(ActionType.FLOOR, Genre.MALE, Direction.S, -1));
        expectedFixedActions.add(new Action(ActionType.END));
        assertEquals(expectedFixedActions, fixedActions);
    }

    @Test
    public void notAllowedEnemyActions_multipleActionNotFixable_unchangedActions() {
        RoundActions unfixedActions = new RoundActions();
        unfixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.SW, 1));
        unfixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.N, 1));
        unfixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 0));
        unfixedActions.add(new Action(ActionType.FLOOR, Genre.MALE, Direction.SW, 1));
        unfixedActions.add(new Action(ActionType.DOME, Genre.MALE, Direction.S, 0));
        unfixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, -1));
        unfixedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.W, 2));
        unfixedActions.add(new Action(ActionType.FLOOR, Genre.MALE, Direction.S, -1));
        unfixedActions.add(new Action(ActionType.END));

        player1.registerAction(new Action(ActionType.MOVE, Genre.FEMALE, Direction.SW, 0));
        player1.registerAction(new Action(ActionType.FLOOR, Genre.FEMALE, Direction.SW, 1));

        RoundActions fixedActions = enemyRules.fixEnemyActions(unfixedActions, game, player1);

        assertEquals(unfixedActions, fixedActions);
    }

    @Test
    public void notAllowedEnemyActions_multipleAlreadyFixedActions_fixedActions() {
        RoundActions unfixedActions = new RoundActions();
        unfixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 0));
        unfixedActions.add(new Action(ActionType.DOME, Genre.MALE, Direction.S, 0));
        unfixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, -1));
        unfixedActions.add(new Action(ActionType.FLOOR, Genre.MALE, Direction.W, 2));
        unfixedActions.add(new Action(ActionType.FLOOR, Genre.MALE, Direction.S, -1));
        unfixedActions.add(new Action(ActionType.END));

        player1.registerAction(new Action(ActionType.MOVE, Genre.FEMALE, Direction.SW, 2));
        player1.registerAction(new Action(ActionType.FLOOR, Genre.FEMALE, Direction.SW, -1));

        RoundActions fixedActions = enemyRules.fixEnemyActions(unfixedActions, game, player1);

        RoundActions expectedFixedActions = new RoundActions();
        expectedFixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 0));
        expectedFixedActions.add(new Action(ActionType.DOME, Genre.MALE, Direction.S, 0));
        expectedFixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, -1));
        expectedFixedActions.add(new Action(ActionType.FLOOR, Genre.MALE, Direction.W, 2));
        expectedFixedActions.add(new Action(ActionType.FLOOR, Genre.MALE, Direction.S, -1));
        expectedFixedActions.add(new Action(ActionType.END));
        assertEquals(expectedFixedActions, fixedActions);
    }

    @Test
    public void notAllowedEnemyActions_noActionsAfterFix_loseCondition() {
        RoundActions unfixedActions = new RoundActions();
        unfixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.SW, 1));

        player1.registerAction(new Action(ActionType.MOVE, Genre.FEMALE, Direction.SW, 2));
        player1.registerAction(new Action(ActionType.DOME, Genre.FEMALE, Direction.SW, 3));

        RoundActions fixedActions = enemyRules.fixEnemyActions(unfixedActions, game, player1);

        RoundActions expectedFixedActions = new RoundActions();
        expectedFixedActions.add(new Action(ActionType.LOSE));
        assertEquals(expectedFixedActions, fixedActions);
    }

    @Test
    public void notAllowedEnemyActions_onlyEndAfterFix_correctBehaviour() {
        RoundActions unfixedActions = new RoundActions();
        unfixedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.SW, 1));
        unfixedActions.add(new Action(ActionType.END));

        player1.registerAction(new Action(ActionType.MOVE, Genre.FEMALE, Direction.SW, 2));
        player1.registerAction(new Action(ActionType.DOME, Genre.FEMALE, Direction.SW, 3));

        RoundActions fixedActions = enemyRules.fixEnemyActions(unfixedActions, game, player1);

        RoundActions expectedFixedActions = new RoundActions();
        expectedFixedActions.add(new Action(ActionType.END));
        assertEquals(expectedFixedActions, fixedActions);
    }
}