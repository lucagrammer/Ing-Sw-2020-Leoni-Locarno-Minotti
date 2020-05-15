package util;

import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.rules.EnemyRules;
import server.rules.Rules;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static util.PlayerColor.*;

public class MapInfoTest {
    private static MapInfo mapInfo;
    private static Game game;
    private static Player player, player1;
    private static Board board;
    private static Worker worker;
    private Cell cell, cell2;
    private static Rules rules;
    private static EnemyRules enemyRules;

    @Before
    public void setUp(){
        game = new Game();
        board = game.getBoard();
        game.setNumPlayers(2);
        player = new Player("John", new Date(4 / 5 / 1996), false);
        player1 = new Player("Mark", new Date(22 / 7 / 1986), false);
        String description = "description";
        Card pan = new Card("Pan", true, description, rules, enemyRules);
        Card apollo = new Card("Apollo", true, description, rules, enemyRules);
        List<Card> usedCards = new ArrayList<>();
        usedCards.add(apollo);
        usedCards.add(pan);
        game.setUsedCards(usedCards);
        player.setCard(apollo);
        player1.setCard(pan);
        player.chooseColor(BLUE);
        player1.chooseColor(YELLOW);
        player.getWorker(Genre.MALE).setPosition(new Cell(1,1));
        player.getWorker(Genre.FEMALE).setPosition(new Cell(3,4));
        player1.getWorker(Genre.MALE).setPosition(new Cell(1,2));
        player1.getWorker(Genre.FEMALE).setPosition(new Cell(2,2));
        game.addPlayer(player);
        game.addPlayer(player1);
    }

    @After
    public void tearDown(){
        game = null;
        mapInfo = null;
        board = null;
    }

    @Test
    public void setColorAt_getColorAt() {
        mapInfo = new MapInfo(game);
        assertEquals(BLUE.toString().toLowerCase(), (mapInfo.getColorAt(1,1)));
        assertNull(mapInfo.getColorAt(4, 2));
    }

    @Test
    public void setDomeAt_getCorrectDomeAt(){
        cell= new Cell (4,2);
        board.getCell(4,2).setDome(true);
        mapInfo = new MapInfo(game);
        assertTrue(mapInfo.getDomeAt(4, 2));
        assertFalse(mapInfo.getDomeAt(1,3));
     }

     @Test
     public void setGenreAt_getCorrectGenreAt(){
        mapInfo = new MapInfo(game);
        assertEquals(Genre.MALE, (mapInfo.getGenreAt(1,1)));
        assertNull(mapInfo.getGenreAt(4, 2));
     }

     @Test
     public void setFloorAt_getCorrectFloorAt(){
        cell = new Cell (0,2);
        board.getCell(0,2).addFloor();
        board.getCell(0,2).addFloor();
        mapInfo = new MapInfo(game);
        assertEquals(2, mapInfo.getFloorAt(0, 2));
     }

     @Test
     public void setNumPlayers_getCorrectNumPlayers(){
        mapInfo = new MapInfo(game);
        assertEquals(2, mapInfo.getNumPlayers());
     }

    @Test
    public void setNicknames_getCorrectNicknames(){
        List<String> playerNames = new ArrayList<>();
        playerNames.add(player.getNickname());
        playerNames.add(player1.getNickname());
        mapInfo = new MapInfo(game);
        assertEquals(playerNames, mapInfo.getNicknames());
     }

     @Test
     public void setColors_getCorrectColors(){
        List<String> colors = new ArrayList<>();
        colors.add(BLUE.toString());
        colors.add(YELLOW.toString());
        mapInfo = new MapInfo(game);
        assertEquals(colors, mapInfo.getColors());
     }

     @Test
     public void setCards_getCorrectCards(){
        List<String> cards = new ArrayList<>();
        cards.add("Apollo");
        cards.add("Pan");
        mapInfo = new MapInfo(game);
        assertEquals(cards, mapInfo.getCards());
     }
}