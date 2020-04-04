package Controller.Rules.SimpleGods;

import Controller.Rules.Rules;
import Model.Board;
import Model.Game;
import Model.Player;
import Util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class PanRulesTest {

    static Rules rules;
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
        player1.chooseColor(Color.WHITE);
        player2.chooseColor(Color.GREY);
        rules = new PanRules();

        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 2));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));
        //System.out.println(nextPossibleActions.getActionList().size());
        //nextPossibleActions.getActionList().stream().forEach(x ->System.out.println(x.getDirection()+" "+ x.getGenre()+" "+x.getActionType()+" "+x.getLevelDifference()));
    }

    @Test
    public void nextPossibleActions_noDomesNoFloorAroundFirstMove_moveToAllFreeCells() {
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
    public void nextPossibleActions_myWorkersAroundFirstAction_moveToAllFreeCells() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 2));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));

        RoundActions nextPossibleActions = rules.nextPossibleActions(player2, game);

        RoundActions expectedActions = new RoundActions();
        //expected male actions
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.W, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.SW, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.SE, 0));

        //expected female actions
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.SE, 0));
        expectedActions.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.E, 0));
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

        RoundActions roundActionWithOneMove = new RoundActions();
        roundActionWithOneMove.add(new Action(ActionType.MOVE, Genre.MALE, Direction.NE, 0));
        player1.setRoundActions(roundActionWithOneMove);
        RoundActions nextPossibleActions = rules.nextPossibleActions(player1, game);

        RoundActions expectedActions = new RoundActions();
        //expected male actions
        expectedActions.add(new Action(ActionType.BUILD_FLOOR, Genre.MALE, Direction.NW, 0));
        expectedActions.add(new Action(ActionType.BUILD_FLOOR, Genre.MALE, Direction.W, 0));
        expectedActions.add(new Action(ActionType.BUILD_FLOOR, Genre.MALE, Direction.SW, 0));
        expectedActions.add(new Action(ActionType.BUILD_FLOOR, Genre.MALE, Direction.S, 0));
        expectedActions.add(new Action(ActionType.BUILD_FLOOR, Genre.MALE, Direction.SE, 0));
        expectedActions.add(new Action(ActionType.BUILD_FLOOR, Genre.MALE, Direction.E, 0));
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

        RoundActions roundActionWithOneMove = new RoundActions();
        roundActionWithOneMove.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.NW, -3));
        player2.setRoundActions(roundActionWithOneMove);
        RoundActions nextPossibleActions = rules.nextPossibleActions(player2, game);

        RoundActions expectedActions = new RoundActions();
        //expected male actions
        expectedActions.add(new Action(ActionType.BUILD_FLOOR, Genre.FEMALE, Direction.S, 2));
        expectedActions.add(new Action(ActionType.BUILD_DOME, Genre.FEMALE, Direction.SE, 3));
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

        RoundActions roundActionWithOneMove = new RoundActions();
        roundActionWithOneMove.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.NW, -3));
        player2.setRoundActions(roundActionWithOneMove);
        RoundActions nextPossibleActions = rules.nextPossibleActions(player2, game);

        RoundActions expectedActions = new RoundActions();
        //expected male actions
        expectedActions.add(new Action(ActionType.LOSE));
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

        Action domeAction = new Action(ActionType.BUILD_DOME, Genre.FEMALE, Direction.S, 3);
        boolean hasWin = rules.doAction(domeAction, player2, game);

        RoundActions playerActions = player2.getRoundActions();
        RoundActions expectedPlayerActions = new RoundActions();
        expectedPlayerActions.add(domeAction);

        assertFalse(hasWin);
        assertEquals(expectedPlayerActions, playerActions);
        assertTrue(board.getCell(1, 3).getDome());
    }

    @Test
    public void nextPossibleActions_AfterBuild_endRound() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 2));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));

        RoundActions roundActionWithMoveAndBuild = new RoundActions();
        roundActionWithMoveAndBuild.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.NW, -3));
        roundActionWithMoveAndBuild.add(new Action(ActionType.BUILD_FLOOR, Genre.FEMALE, Direction.S, 0));
        player2.setRoundActions(roundActionWithMoveAndBuild);
        RoundActions nextPossibleActions = rules.nextPossibleActions(player2, game);

        RoundActions expectedActions = new RoundActions();
        //expected male actions
        expectedActions.add(new Action(ActionType.END));
        assertEquals(expectedActions, nextPossibleActions);
    }

    @Test
    public void doAction_doBuild_build() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 2));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));

        board.getCell(1, 3).addFloor();

        Action buildAction = new Action(ActionType.BUILD_FLOOR, Genre.FEMALE, Direction.S, 1);
        boolean hasWin = rules.doAction(buildAction, player2, game);

        RoundActions playerActions = player2.getRoundActions();
        RoundActions expectedPlayerActions = new RoundActions();
        expectedPlayerActions.add(buildAction);

        assertFalse(hasWin);
        assertEquals(expectedPlayerActions, playerActions);
        assertEquals(board.getCell(1, 3).getFloor(), 2);
    }

    @Test
    public void doAction_doMove_PanWinningMove() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 2));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));

        //RoundActions nextPossibleActions = rules.nextPossibleActions(player2, game);

        board.getCell(0, 3).addFloor();
        board.getCell(0, 3).addFloor();
        board.getCell(0,3).addFloor();

        Action moveAction = new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, -3);
        boolean hasWin = rules.doAction(moveAction, player2, game);

        RoundActions playerActions = player2.getRoundActions();
        RoundActions expectedPlayerActions = new RoundActions();
        expectedPlayerActions.add(moveAction);

        assertTrue(hasWin);
        assertEquals(expectedPlayerActions, playerActions);
        assertEquals(player2.getWorker(Genre.FEMALE).getPosition(), board.getCell(1, 3));
    }

    @Test
    public void doAction_doMove_notWinningMove() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 2));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));

        board.getCell(0, 3).addFloor();
        board.getCell(1, 3).addFloor();

        Action moveAction = new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 1);
        boolean hasWin = rules.doAction(moveAction, player2, game);

        RoundActions playerActions = player2.getRoundActions();
        RoundActions expectedPlayerActions = new RoundActions();
        expectedPlayerActions.add(moveAction);

        assertFalse(hasWin);
        assertEquals(expectedPlayerActions, playerActions);
        assertEquals(player2.getWorker(Genre.FEMALE).getPosition(), board.getCell(1, 3));
    }

    @Test
    public void doAction_doEnd_nothingToDo() {
        player1.getWorker(Genre.MALE).setPosition(board.getCell(1, 2));
        player1.getWorker(Genre.FEMALE).setPosition(board.getCell(3, 4));

        player2.getWorker(Genre.MALE).setPosition(board.getCell(0, 2));
        player2.getWorker(Genre.FEMALE).setPosition(board.getCell(0, 3));

        Action endAction = new Action(ActionType.END);
        boolean hasWin = rules.doAction(endAction, player2, game);

        assertFalse(hasWin);
        assertEquals(player2.getWorker(Genre.FEMALE).getPosition(), board.getCell(0, 3));
    }

}