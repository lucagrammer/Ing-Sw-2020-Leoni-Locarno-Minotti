package Server.Rules.SimpleGods;

import Model.Board;
import Model.Game;
import Model.Player;
import Server.Rules.EnemyRules;
import Util.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class AthenaEnemyRulesTest {
    static EnemyRules enemyRules;
    static Game game;
    static Board board;
    static Player player1, player2;

    @Before
    public void setUp() {
        player1 = new Player("AlphaTester", new Date(System.currentTimeMillis()));
        player2 = new Player("BetaTester", new Date(System.currentTimeMillis()));
        game = new Game(player1, 2);
        game.addPlayer(player2);
        board = game.getBoard();
        player1.chooseColor(Color.PURPLE);
        player2.chooseColor(Color.YELLOW);
        enemyRules = new AthenaEnemyRules();
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