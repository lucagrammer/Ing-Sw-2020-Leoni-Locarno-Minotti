package util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RoundActionsTest {
    private static Action moveAction, floorAction, domeAction, endAction;


    @Test
    public void setActionList_GetCorrectActionList() {
        RoundActions roundActions=new RoundActions();
        List<Action> actionList=new ArrayList<>();
        moveAction=new Action(ActionType.MOVE,Genre.MALE,Direction.N,1);
        floorAction=new Action(ActionType.FLOOR,Genre.MALE,Direction.S,1);
        roundActions.add(moveAction);
        roundActions.add(floorAction);
        actionList.add(moveAction);
        actionList.add(floorAction);
        assertEquals(actionList,roundActions.getActionList());
    }

    @Test
    public void doMove_getCorrectNumOfMovement() {
        RoundActions roundActions=new RoundActions();
        moveAction=new Action(ActionType.MOVE,Genre.MALE,Direction.N,1);
        Action moveAction2;
        moveAction2= new Action(ActionType.MOVE, Genre.FEMALE, Direction.S,0);
        roundActions.add(moveAction);
        roundActions.add(moveAction2);
        assertEquals(2,roundActions.hasMoved());
    }

    @Test
    public void doBuild_getCorrectNumOfBuildings() {
        RoundActions roundActions=new RoundActions();
        floorAction=new Action(ActionType.FLOOR,Genre.MALE,Direction.N,1);
        Action floorAction2;
        floorAction2= new Action(ActionType.FLOOR, Genre.FEMALE, Direction.S,0);
        domeAction= new Action(ActionType.DOME, Genre.FEMALE, Direction.NW,1);
        roundActions.add(floorAction);
        roundActions.add(floorAction2);
        roundActions.add(domeAction);
        assertEquals(3,roundActions.hasBuildAnything());
    }

    @Test
    public void getCorrectIsEmpty() {
        RoundActions roundActions=new RoundActions();
        assertTrue(roundActions.isEmpty());
        moveAction=new Action(ActionType.MOVE,Genre.MALE,Direction.N,1);
        roundActions.add(moveAction);
        assertFalse(roundActions.isEmpty());
    }


    @Test
    public void doBuild_getCorrectNumOfFloorBuildings() {
        RoundActions roundActions=new RoundActions();
        floorAction=new Action(ActionType.FLOOR,Genre.MALE,Direction.N,1);
        Action floorAction2;
        floorAction2= new Action(ActionType.FLOOR, Genre.FEMALE, Direction.S,0);
        domeAction= new Action(ActionType.DOME, Genre.FEMALE, Direction.NW,1);
        roundActions.add(floorAction);
        roundActions.add(floorAction2);
        roundActions.add(domeAction);
        assertEquals(2,roundActions.hasBuildFloor());
    }

    @Test
    public void movedUp_GetCorrectHasMovedUp() {
        RoundActions roundActions=new RoundActions();
        moveAction=new Action(ActionType.MOVE,Genre.MALE,Direction.N,1);
        roundActions.add(moveAction);
        assertTrue(roundActions.hasMovedUp());
    }

    @Test
    public void movedDown_GetCorrectHasMovedUp() {
        RoundActions roundActions=new RoundActions();
        moveAction=new Action(ActionType.MOVE,Genre.MALE,Direction.N,-1);
        roundActions.add(moveAction);
        assertFalse(roundActions.hasMovedUp());
    }

    @Test
    public void SetAction_FindCorrectAction() {
        RoundActions roundActions=new RoundActions();
        moveAction=new Action(ActionType.MOVE,Genre.FEMALE,Direction.SW,0);
        roundActions.add(moveAction);
        String move = "MOVE";
        String female = "F";
        String direction = "SW";
        assertEquals(moveAction, roundActions.find(move, female, direction));
        floorAction= new Action(ActionType.FLOOR, Genre.MALE, Direction.SW, 0);
        roundActions.add(floorAction);
        String floor ="FLOOR";
        String male= "M";
        assertEquals(floorAction, roundActions.find(floor, male, direction));
        String wrong= "WRONG";
        assertNull(roundActions.find(wrong, female, direction));
        assertNull(roundActions.find(move, wrong, direction));
    }

    @Test
    public void setRoundAction_getCorrectCanEnd() {
        RoundActions roundActions= new RoundActions();
        moveAction=new Action(ActionType.MOVE,Genre.FEMALE,Direction.SW,0);
        roundActions.add(moveAction);
        assertNull(roundActions.findEnd());
        endAction = new Action(ActionType.END, Genre.FEMALE, Direction.NW, 0);
        roundActions.add(endAction);
        assertEquals(endAction, roundActions.findEnd());
    }

    @Test
    public void setEndAction_getCorrectHasEnded() {
        RoundActions roundActions= new RoundActions();
        endAction= new Action(ActionType.END, Genre.FEMALE, Direction.NW, 0);
        roundActions.add(endAction);
        assertTrue(roundActions.hasEnded());
    }

    @Test
    public void setRoundAction_getCorrectHasLost() {
        RoundActions roundActions= new RoundActions();
        Action loseAction = new Action(ActionType.LOSE, Genre.FEMALE, Direction.SW, 0);
        roundActions.add(loseAction);
        assertTrue(roundActions.hasLost());
    }

    @Test
    public void setRoundAction_getCorrectMustEnd() {
        RoundActions roundActions= new RoundActions();
        endAction= new Action(ActionType.END, Genre.FEMALE, Direction.NW, 0);
        roundActions.add(endAction);
        assertTrue(roundActions.mustEnd());
    }
}