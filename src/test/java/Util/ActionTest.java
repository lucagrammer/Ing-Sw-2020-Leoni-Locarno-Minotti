package Util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActionTest {
    Action action;

    @Before
    public void setUp() {
        action = new Action(ActionType.MOVE, Genre.MALE, Direction.N, 1);
    }

    @After
    public void tearDown() {
        action = null;
    }

    @Test
    public void action_actionSetUp_correctSetUp() {
        Genre genre = Genre.FEMALE;
        ActionType actionType = ActionType.FLOOR;
        Direction direction = Direction.NE;
        int levelDifference = -2;

        action = new Action(actionType, genre, direction, levelDifference);
        assertEquals(actionType, action.getActionType());
        assertEquals(genre, action.getGenre());
        assertEquals(direction, action.getDirection());
        assertEquals(levelDifference, action.getLevelDifference());
    }

    @Test
    public void genreInfo_setGenre_getCorrectGenre() {
        Genre genre = Genre.MALE;
        action.setGenre(genre);
        assertEquals(genre, action.getGenre());
    }

    @Test
    public void actionInfo_setAction_getCorrectAction() {
        ActionType type = ActionType.DOME;
        action.setActionType(type);
        assertEquals(type, action.getActionType());
    }

    @Test
    public void directionInfo_setDirection_getCorrectDirection() {
        Direction direction = Direction.N;
        action.setDirection(direction);
        assertEquals(direction, action.getDirection());
    }

    @Test
    public void levelDifferenceInfo_setLevelDifference_getCorrectLevelDifference() {
        int level = 1;
        action.setLevelDifference(level);
        assertEquals(level, action.getLevelDifference());
    }
}