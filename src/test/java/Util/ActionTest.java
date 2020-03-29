package Util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ActionTest {
    Action action;

    @Before
    public void setUp() {
        action = new Action(ActionType.MOVE, Genre.MALE, Direction.N, 1, true, false);
    }

    @After
    public void tearDown() {
        action = null;
    }

    @Test
    public void action_actionSetUp_correctSetUp() {
        Genre genre = Genre.FEMALE;
        ActionType actionType = ActionType.OPT_BUILD;
        Direction direction = Direction.NE;
        boolean winning = true, influenceRivals = false;
        int levelDifference = -2;

        action = new Action(actionType, genre, direction, levelDifference, winning, influenceRivals);
        assertEquals(actionType, action.getActionType());
        assertEquals(genre, action.getGenre());
        assertEquals(direction, action.getDirection());
        assertEquals(levelDifference, action.getLevelDifference());
        assertEquals(winning, action.isWinning());
        assertEquals(influenceRivals, action.isInfluenceRivals());
    }

    @Test
    public void genreInfo_setGenre_getCorrectGenre() {
        Genre genre = Genre.MALE;
        action.setGenre(genre);
        assertEquals(genre, action.getGenre());
    }

    @Test
    public void actionInfo_setAction_getCorrectAction() {
        ActionType type = ActionType.BUILD;
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

    @Test
    public void winningInfo_setWinning_getCorrectWinning() {
        boolean winning = true;
        action.setWinning(winning);
        assertEquals(winning, action.isWinning());
    }

    @Test
    public void influenceRivalsInfo_setInfluenceRival_getCorrectInfluenceRival() {
        boolean influence = false;
        action.setInfluenceRivals(influence);
        assertEquals(influence, action.isInfluenceRivals());
    }

}