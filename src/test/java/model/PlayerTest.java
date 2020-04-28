package model;

import Server.Rules.EnemyRules;
import Server.Rules.Rules;
import Util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerTest {
    Player player;
    Date date;
    Rules rules;
    EnemyRules enemyRules;

    @Before
    public void setUp(){
        date = new Date(3/2/1998);
        player = new Player("John", date);
    }

    @After
    public void tearDown(){ player = null;
    }

    @Test
    public void getNickname_nickname_getCorrectNickname(){
        String nickname = "John";
        assertEquals(nickname, player.getNickname());
    }

    @Test
    public void getDateOfBirth_dateOfBirth_getCorrectDateOfBirth(){
        Date dateOfBirth = new Date(3/2/1998);
        assertEquals(dateOfBirth, player.getDateOfBirth());
    }

    @Test
    public void equals_player_CorrectEquals(){
        Player newPlayer = new Player("John", new Date(3/2/1998));
        Player newPlayer1 = new Player("Jonah", new Date(7/12/1998));
        assertTrue(player.equals(newPlayer));
        assertFalse(player.equals(newPlayer1));
    }

    @Test
    public void card_setCard_getCorrectCard(){
        Card card = new Card("Apollo", true, "Description Apollo", rules, enemyRules);
        player.setCard(card);
        assertEquals(card, player.getCard());
    }

    @Test
    public void chooseColor_color_getWorker(){
        Color color = Color.PURPLE;
        player.chooseColor(color);
        assertEquals(color, player.getWorker(Genre.FEMALE).getColor());
        assertEquals(color, player.getWorker(Genre.MALE).getColor());
    }

    @Test
    public void chooseColor_color_getColor(){
        Color color = Color.PURPLE;
        player.chooseColor(color);
        assertEquals(color, player.getColor());
    }

    @Test
    public void occupiedCell_setOccupiedCell_getOccupiedCell(){
        player.getWorker(Genre.MALE).setPosition(new Cell(1,3));
        player.getWorker(Genre.FEMALE).setPosition(new Cell(2,3));

        List<Cell> expected = new ArrayList<>();
        expected.add(new Cell(2,3));
        expected.add(new Cell(1,3));
        assertEquals(expected, player.getOccupiedCells());
    }

    @Test
    public void roundActions_setRoundActions_getRoundActions(){
        RoundActions expectedRoundActions = new RoundActions();
        player.setRoundActions(expectedRoundActions);
        assertEquals(expectedRoundActions, player.getRoundActions());
    }

    @Test
    public void worker_position_getWorkerByPosition(){
        player.getWorker(Genre.MALE).setPosition(new Cell(1,3));
        player.getWorker(Genre.FEMALE).setPosition(new Cell(2,3));

        Worker expectedMale = new Worker(Genre.MALE, player);
        Worker expectedFemale = new Worker(Genre.FEMALE, player);
        Cell maleCell = new Cell(1,3);
        Cell femaleCell = new Cell(2,3);
        Cell nullCell = new Cell(3,3);
        expectedFemale.setPosition(femaleCell);
        expectedMale.setPosition(maleCell);
        assertEquals(expectedFemale.getPlayer().getNickname(), player.getWorkerByPosition(femaleCell).getPlayer().getNickname());
        assertEquals(expectedMale.getPlayer().getNickname(), player.getWorkerByPosition(maleCell).getPlayer().getNickname());
        assertNull(player.getWorkerByPosition(nullCell));
    }

    @Test
    public void player_action_registerActions(){
        player.chooseColor(Color.PURPLE);
        Action firstMove = new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 0);
        player.registerAction(firstMove);
        Action firstBuild = new Action(ActionType.FLOOR, Genre.FEMALE, Direction.S, 1);
        player.registerAction(firstBuild);
        RoundActions expected = new RoundActions();
        expected.add(new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 0));
        expected.add(new Action(ActionType.FLOOR, Genre.FEMALE, Direction.S, 1));
        assertEquals(expected, player.getRoundActions());
    }
}