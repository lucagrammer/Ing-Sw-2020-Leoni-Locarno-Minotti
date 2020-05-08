package server.rules.simpleGods;

import model.Board;
import model.Game;
import model.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.rules.Rules;
import util.*;

import java.util.Date;

import static org.junit.Assert.*;

public class MinotaurRulesTest {
    private static Rules rules;
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
        rules = new MinotaurRules();
    }

    @After
    public void tearDown() {
        rules = null;
        game = null;
        board = null;
        player1 = null;
        player2 = null;
    }

    @Test
    public void nextPossibleActions_noDomesNoFloorAroundFirstMove_regularMove() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 2));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));

        RoundActions nextPossibleActions = rules.nextPossibleActions(player1, game);

        RoundActions expectedActions = new RoundActions();
        //expected male actions
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.NW, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.W, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.SW, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.S, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.SE, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.E, 0));

        //expected female actions
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.NW, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.W, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.SW, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.N, 0));

        assertEquals(expectedActions, nextPossibleActions);
    }

    @Test
    public void nextPossibleActions_myWorkersAroundFirstAction_regularMoveAndMinotaurPower() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 2));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));

        RoundActions nextPossibleActions = rules.nextPossibleActions(player2, game);

        RoundActions expectedActions = new RoundActions();
        //expected male actions
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.W, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.SW, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.S, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.SE, 0));

        //expected female actions
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.SW, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.SE, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.E, 0));
        assertEquals(expectedActions, nextPossibleActions);
    }

    @Test
    public void nextPossibleActions_enemyNextCellOccupiedByMyWorkerAtFirstAction_regularMove() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 3));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 1));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(2, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 4));

        board.getCell(4, 0).addFloor();
        board.getCell(4, 0).addFloor();

        RoundActions nextPossibleActions = rules.nextPossibleActions(player2, game);

        RoundActions expectedActions = new RoundActions();
        //expected male actions
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.NW, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.W, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.SW, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.S, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.SE, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.E, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.N, 0));

        //expected female actions
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.W, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 0));

        assertEquals(expectedActions, nextPossibleActions);
    }

    @Test
    public void nextPossibleActions_enemyNextCellOccupiedByEnemyFirstAction_regularMove() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 3));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(2, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 1));

        RoundActions nextPossibleActions = rules.nextPossibleActions(player2, game);

        RoundActions expectedActions = new RoundActions();
        //expected male actions
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.NW, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.W, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.S, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.SE, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.E, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.N, 0));

        //expected female actions
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.NW, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.W, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.SW, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.SE, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.E, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.N, 0));

        assertEquals(expectedActions, nextPossibleActions);
    }

    @Test
    public void nextPossibleActions_enemyCellTooHighOrEnemyNextCellOccupiedByDomeAtFirstAction_regularMove() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 3));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 1));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(2, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(2, 4));

        board.getCell(4, 0).setDome(true);
        board.getCell(3, 1).addFloor();
        board.getCell(1, 3).addFloor();
        board.getCell(1, 3).addFloor();

        RoundActions nextPossibleActions = rules.nextPossibleActions(player2, game);

        RoundActions expectedActions = new RoundActions();
        //expected male actions
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.NW, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.W, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.S, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.SE, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.E, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.N, 0));

        //expected female actions
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.N, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.W, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.SW, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 0));

        assertEquals(expectedActions, nextPossibleActions);
    }

    @Test
    public void nextPossibleActions_allOccupiedAtFirstAction_LoseCondition() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 2));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));

        board.getCell(1, 3).addFloor();
        board.getCell(1, 3).addFloor();
        board.getCell(1, 4).addFloor();
        board.getCell(1, 4).addFloor();
        board.getCell(1, 4).addFloor();
        board.getCell(1, 2).addFloor();
        board.getCell(1, 2).addFloor();
        board.getCell(0, 4).setDome(true);
        board.getCell(0, 1).setDome(true);
        board.getCell(1, 1).setDome(true);

        RoundActions nextPossibleActions = rules.nextPossibleActions(player2, game);

        RoundActions expectedActions = new RoundActions();
        //expected actions
        expectedActions.add(new Action(ActionType.LOSE));

        assertEquals(expectedActions, nextPossibleActions);
    }

    @Test
    public void nextPossibleActions_noDomesNoFloorAroundAfterMove_buildAllFreeCells() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 2));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));

        RoundActions raWithOneMove = new RoundActions();
        raWithOneMove.add(new Action(ActionType.MOVE, Genre.MALE, Direction.NE, 0));
        player1.setRoundActions(raWithOneMove);
        RoundActions nextPossibleActions = rules.nextPossibleActions(player1, game);

        RoundActions expectedActions = new RoundActions();
        //expected male actions
        expectedActions.add(new Action(ActionType.FLOOR, Genre.MALE, Direction.NW, 0));
        expectedActions.add(new Action(ActionType.FLOOR, Genre.MALE, Direction.W, 0));
        expectedActions.add(new Action(ActionType.FLOOR, Genre.MALE, Direction.SW, 0));
        expectedActions.add(new Action(ActionType.FLOOR, Genre.MALE, Direction.S, 0));
        expectedActions.add(new Action(ActionType.FLOOR, Genre.MALE, Direction.SE, 0));
        expectedActions.add(new Action(ActionType.FLOOR, Genre.MALE, Direction.E, 0));

        assertEquals(expectedActions, nextPossibleActions);
    }

    @Test
    public void nextPossibleActions_multipleFloorsAfterMove_buildAllCorrectCells() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 2));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));

        board.getCell(1, 3).addFloor();
        board.getCell(1, 3).addFloor();
        board.getCell(1, 4).addFloor();
        board.getCell(1, 4).addFloor();
        board.getCell(1, 4).addFloor();
        board.getCell(0, 4).setDome(true);

        RoundActions raWithOneMove = new RoundActions();
        raWithOneMove.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.NW, -3));
        player2.setRoundActions(raWithOneMove);
        RoundActions nextPossibleActions = rules.nextPossibleActions(player2, game);

        RoundActions expectedActions = new RoundActions();
        //expected male actions
        expectedActions.add(new Action(ActionType.FLOOR, Genre.FEMALE, Direction.S, 2));
        expectedActions.add(new Action(ActionType.DOME, Genre.FEMALE, Direction.SE, 3));
        assertEquals(expectedActions, nextPossibleActions);
    }

    @Test
    public void nextPossibleActions_allOccupiedAfterMove_LoseCondition() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 2));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));

        board.getCell(1, 3).addFloor();
        board.getCell(1, 3).addFloor();
        board.getCell(1, 4).addFloor();
        board.getCell(1, 4).addFloor();
        board.getCell(1, 4).addFloor();
        board.getCell(0, 4).setDome(true);
        board.getCell(1, 3).setDome(true);
        board.getCell(1, 4).setDome(true);

        RoundActions raWithOneMove = new RoundActions();
        raWithOneMove.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.NW, -3));
        player2.setRoundActions(raWithOneMove);
        RoundActions nextPossibleActions = rules.nextPossibleActions(player2, game);

        RoundActions expectedActions = new RoundActions();
        //expected male actions
        expectedActions.add(new Action(ActionType.LOSE));
        assertEquals(expectedActions, nextPossibleActions);
    }

    @Test
    public void nextPossibleActions_AfterBuild_endRound() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 2));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));

        RoundActions raWithMoveAndBuild = new RoundActions();
        raWithMoveAndBuild.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.NW, -3));
        raWithMoveAndBuild.add(new Action(ActionType.FLOOR, Genre.FEMALE, Direction.S, 0));
        player2.setRoundActions(raWithMoveAndBuild);
        RoundActions nextPossibleActions = rules.nextPossibleActions(player2, game);

        RoundActions expectedActions = new RoundActions();
        //expected male actions
        expectedActions.add(new Action(ActionType.END));
        assertEquals(expectedActions, nextPossibleActions);
    }

    @Test
    public void doAction_doDome_dome() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 2));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));

        board.getCell(1, 3).addFloor();
        board.getCell(1, 3).addFloor();
        board.getCell(1, 3).addFloor();

        Action domeAction = new Action(ActionType.DOME, Genre.FEMALE, Direction.S, 3);
        boolean hasWin = rules.doAction(domeAction, player2, game);

        RoundActions playerActions = player2.getRoundActions();
        RoundActions expectedActions = new RoundActions();
        expectedActions.add(domeAction);

        assertFalse(hasWin);
        assertEquals(expectedActions, playerActions);
        assertTrue(board.getCell(1, 3).getDome());
    }

    @Test
    public void doAction_doBuild_build() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 2));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));

        board.getCell(1, 3).addFloor();

        Action buildAction = new Action(ActionType.FLOOR, Genre.FEMALE, Direction.S, 1);
        boolean hasWin = rules.doAction(buildAction, player2, game);

        RoundActions playerActions = player2.getRoundActions();
        RoundActions expectedActions = new RoundActions();
        expectedActions.add(buildAction);

        assertFalse(hasWin);
        assertEquals(expectedActions, playerActions);
        assertEquals(board.getCell(1, 3).getFloor(), 2);
    }

    @Test
    public void doAction_doRegularMove_winningMove() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 2));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));

        board.getCell(0, 3).addFloor();
        board.getCell(0, 3).addFloor();
        board.getCell(1, 3).addFloor();
        board.getCell(1, 3).addFloor();
        board.getCell(1, 3).addFloor();

        Action moveAction = new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 1);
        boolean hasWin = rules.doAction(moveAction, player2, game);

        RoundActions playerActions = player2.getRoundActions();
        RoundActions expectedActions = new RoundActions();
        expectedActions.add(moveAction);

        assertTrue(hasWin);
        assertEquals(expectedActions, playerActions);
        assertEquals(player2.getWorker(Genre.FEMALE).getPosition(), board.getCell(1, 3));
    }

    @Test
    public void doAction_doRegularMove_notWinningMove() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 2));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));

        board.getCell(0, 3).addFloor();
        board.getCell(1, 3).addFloor();

        Action moveAction = new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 1);
        boolean hasWin = rules.doAction(moveAction, player2, game);

        RoundActions playerActions = player2.getRoundActions();
        RoundActions expectedActions = new RoundActions();
        expectedActions.add(moveAction);

        assertFalse(hasWin);
        assertEquals(expectedActions, playerActions);
        assertEquals(player2.getWorker(Genre.FEMALE).getPosition(), board.getCell(1, 3));
    }

    @Test
    public void doAction_doMinotaurMove_notWinningMove() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 3));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));

        board.getCell(0, 2).addFloor();
        board.getCell(1, 3).addFloor();
        board.getCell(1, 3).addFloor();

        Action moveAction = new Action(ActionType.MOVE, Genre.MALE, Direction.SE, 1);
        boolean hasWin = rules.doAction(moveAction, player2, game);

        RoundActions playerActions = player2.getRoundActions();
        RoundActions expectedActions = new RoundActions();
        expectedActions.add(moveAction);

        assertFalse(hasWin);
        assertEquals(expectedActions, playerActions);
        assertEquals(player2.getWorker(Genre.MALE).getPosition(), board.getCell(1, 3));
        assertEquals(player1.getWorker(Genre.MALE).getPosition(), board.getCell(2, 4));
    }

    @Test
    public void doAction_doMinotaurMove_winningMove() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 3));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));

        board.getCell(0, 3).addFloor();
        board.getCell(0, 3).addFloor();
        board.getCell(1, 3).addFloor();
        board.getCell(1, 3).addFloor();
        board.getCell(1, 3).addFloor();

        Action moveAction = new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 1);
        boolean hasWin = rules.doAction(moveAction, player2, game);

        RoundActions playerActions = player2.getRoundActions();
        RoundActions expectedActions = new RoundActions();
        expectedActions.add(moveAction);

        assertTrue(hasWin);
        assertEquals(expectedActions, playerActions);
        assertEquals(player2.getWorker(Genre.FEMALE).getPosition(), board.getCell(1, 3));
        assertEquals(player1.getWorker(Genre.MALE).getPosition(), board.getCell(2, 3));
    }
}