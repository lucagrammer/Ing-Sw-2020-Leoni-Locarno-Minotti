package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.rules.EnemyRules;
import server.rules.Rules;
import util.PlayerColor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class GameTest {
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Player player;
    private Game game;
    private Rules rules;
    private EnemyRules enemyRules;

    @Before
    public void setUp() throws ParseException {
        String birthDate = "03/01/1998";
        Date dateOfBirth = dateFormat.parse(birthDate);
        player = new Player("John", dateOfBirth, false);
        rules = new Rules();
        enemyRules = new EnemyRules();
        game = new Game();
        game.setNumPlayers(3);
        game.addPlayer(player);
    }

    @After
    public void tearDown() {
        player = null;
        game = null;
        rules = null;
        enemyRules = null;
    }

    @Test
    public void addPlayer_addPlayer_PlayersSorted() throws ParseException {
        String birthDate = "02/01/1998";
        Date dateOfBirth = dateFormat.parse(birthDate);
        Player player1 = new Player("Kate", dateOfBirth, false);
        String birthDate2 = "01/01/1998";
        Date dateOfBirth2 = dateFormat.parse(birthDate2);
        Player player2 = new Player("Julie", dateOfBirth2, false);
        game.addPlayer(player1);
        game.addPlayer(player2);
        int index0 = 0;
        int index1 = 1;
        int index2 = 2;
        assertEquals(index0, game.getPlayers().indexOf(player));
        assertEquals(index1, game.getPlayers().indexOf(player1));
        assertEquals(index2, game.getPlayers().indexOf(player2));
    }

    @Test
    public void getNextPlayer_nextPlayer_correctNextPlayer() throws ParseException {
        String birthDate = "02/01/1998";
        Date dateOfBirth = dateFormat.parse(birthDate);
        Player player1 = new Player("Kate", dateOfBirth, false);
        String birthDate2 = "01/01/1998";
        Date dateOfBirth2 = dateFormat.parse(birthDate2);
        Player player2 = new Player("Julie", dateOfBirth2, false);
        String birthDate3 = "01/04/1998/";
        Date dateOfBirth3 = dateFormat.parse(birthDate3);
        Player player3 = new Player("Mary", dateOfBirth3, false);
        game.addPlayer(player1);
        game.addPlayer(player2);
        assertEquals(player1, game.getNextPlayer(player));
        assertEquals(player2, game.getNextPlayer(player1));
        assertEquals(player, game.getNextPlayer(null));
        assertEquals(player, game.getNextPlayer(player2));
        assertEquals(player, game.getNextPlayer(player3));
        player2.setConnected(false);
        player2.setLoser(false);
        assertEquals(player, game.getNextPlayer(player1));
    }

    /*@Test
    public void getPlayer_Players_CorrectPlayer(){
        Player player1 = new Player("Kate", new Date(1/5/1998));
        Player player2 = new Player("Julie", new Date(2/5/1998));
        game.addPlayer(player1);
        game.addPlayer(player2);
        assertEquals(players, game.getPlayers());
    }*/

    @Test
    public void card_setUsedCards_getUsedCards() {
        List<Card> usedCards = new ArrayList<>();
        game.setUsedCards(usedCards);
        assertEquals(usedCards, game.getUsedCards());
    }

    /*@Test
    public void getBoard_gameBoard_getCorrectBoard(){
        Board board = new Board(game);
        assertEquals(board, game.getBoard());
    }*/

    @Test
    public void isReady_game_readyToStart() {
        List<Card> usedCards = new ArrayList<>();
        Card card1 = new Card("Apollo", true, "Apollo", rules, enemyRules);
        Card card2 = new Card("Artemis", true, "Artemis", rules, enemyRules);
        Card card3 = new Card("Athena", true, "Athena", rules, enemyRules);
        usedCards.add(card1);
        usedCards.add(card2);
        usedCards.add(card3);
        game.setUsedCards(usedCards);
        Player player1 = new Player("Kate", new Date(1 / 5 / 1998), false);
        Player player2 = new Player("Julie", new Date(2 / 5 / 1998), false);
        game.addPlayer(player1);
        game.addPlayer(player2);
        assertTrue(game.canStart());
    }

    @Test
    public void isReady_game_notReadyToStart() {
        List<Card> usedCards = new ArrayList<>();
        game.setUsedCards(usedCards);
        Player player1 = new Player("Kate", new Date(1 / 5 / 1998), false);
        game.addPlayer(player1);
        assertFalse(game.canStart());
        Player player2 = new Player("Temporary", new Date(3 / 4 / 1999), true);
        game.addPlayer(player2);
        assertFalse(game.canStart());
    }

    @Test
    public void getPlayersNicknames_game_getCorrectPlayersNicknames() throws ParseException {
        String birthDate = "02/01/1998";
        Date dateOfBirth = dateFormat.parse(birthDate);
        Player player1 = new Player("Kate", dateOfBirth, false);
        String birthDate2 = "01/01/1998";
        Date dateOfBirth2 = dateFormat.parse(birthDate2);
        Player player2 = new Player("Julie", dateOfBirth2, false);
        game.addPlayer(player1);
        game.addPlayer(player2);
        List<String> expectedNicknames = new ArrayList<>();
        expectedNicknames.add("John");
        expectedNicknames.add("Kate");
        expectedNicknames.add("Julie");
        assertEquals(expectedNicknames, game.getPlayersNickname());
    }

    @Test
    public void getPlayerByColor_game_getCorrectPlayerByColor() throws ParseException {
        String birthDate = "02/01/1998";
        Date dateOfBirth = dateFormat.parse(birthDate);
        Player player1 = new Player("Kate", dateOfBirth, false);
        String birthDate2 = "01/01/1998";
        Date dateOfBirth2 = dateFormat.parse(birthDate2);
        Player player2 = new Player("Julie", dateOfBirth2, false);
        game.addPlayer(player1);
        game.addPlayer(player2);
        player.chooseColor(PlayerColor.BLUE);
        player1.chooseColor(PlayerColor.PURPLE);
        assertEquals(player, game.getPlayerByColor(PlayerColor.BLUE));
        assertEquals(player1, game.getPlayerByColor(PlayerColor.PURPLE));
        assertNull(game.getPlayerByColor(PlayerColor.YELLOW));
        player2.chooseColor(PlayerColor.YELLOW);
        assertEquals(player2, game.getPlayerByColor(PlayerColor.YELLOW));
    }

    @Test
    public void getPlayerByNickname_game_getCorrectPlayerByNickname() throws ParseException {
        String birthDate = "02/01/1998";
        Date dateOfBirth = dateFormat.parse(birthDate);
        Player player1 = new Player("Kate", dateOfBirth, false);
        String birthDate2 = "01/01/1998";
        Date dateOfBirth2 = dateFormat.parse(birthDate2);
        Player player2 = new Player("Julie", dateOfBirth2, false);
        game.addPlayer(player1);
        game.addPlayer(player2);
        String nickname = player.getNickname();
        String nickname1 = player1.getNickname();
        String nickname2 = player2.getNickname();
        String nickname3 = "Elisabeth";

        assertEquals(player, game.getPlayerByNickname(nickname));
        assertEquals(player1, game.getPlayerByNickname(nickname1));
        assertEquals(player2, game.getPlayerByNickname(nickname2));
        assertNull(game.getPlayerByNickname(nickname3));
    }

    @Test
    public void hasWinner_game_itHasAWinner() throws ParseException {
        String birthDate = "02/01/1998";
        Date dateOfBirth = dateFormat.parse(birthDate);
        Player player1 = new Player("Kate", dateOfBirth, false);
        String birthDate2 = "01/01/1998";
        Date dateOfBirth2 = dateFormat.parse(birthDate2);
        Player player2 = new Player("Julie", dateOfBirth2, false);
        game.addPlayer(player1);
        game.addPlayer(player2);
        player1.setWinner(true);
        assertTrue(game.hasWinner());
    }

    @Test
    public void hasWinner_game_itHasNotAWinner() throws ParseException {
        String birthDate = "02/01/1998";
        Date dateOfBirth = dateFormat.parse(birthDate);
        Player player1 = new Player("Kate", dateOfBirth, false);
        String birthDate2 = "01/01/1998";
        Date dateOfBirth2 = dateFormat.parse(birthDate2);
        Player player2 = new Player("Julie", dateOfBirth2, false);
        game.addPlayer(player1);
        game.addPlayer(player2);
        assertFalse(game.hasWinner());
    }

    @Test
    public void getBoard_game_getCorrectBoard() {
        Board board = new Board();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(board.getCell(i, j), game.getBoard().getCell(i, j));
            }
        }
    }

    @Test
    public void setAllPlayers_GetAllCorrectPlayers() throws ParseException {
        String birthDate = "02/01/1998";
        Date dateOfBirth = dateFormat.parse(birthDate);
        List<Player> AllPlayers= new ArrayList<>();
        Player player1 = new Player("Kate", dateOfBirth, false);
        String birthDate2 = "01/01/1998";
        Date dateOfBirth2 = dateFormat.parse(birthDate2);
        Player player2 = new Player("Julie", dateOfBirth2, false);
        game.addPlayer(player1);
        game.addPlayer(player2);
        AllPlayers.add(player);
        AllPlayers.add(player1);
        AllPlayers.add(player2);
        assertEquals(AllPlayers, game.getAllPlayers());
    }

    @Test
    public void GetCorrectNumPlayers() {
        assertEquals(3, game.getNumPlayers());
    }

    @Test
    public void setIsActive_GetCorrectIsActive() {
        assertTrue(game.isActive());
        game.setInactive();
        assertFalse(game.isActive());
    }

}
