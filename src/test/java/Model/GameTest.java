package Model;

import Server.Rules.EnemyRules;
import Server.Rules.Rules;
import Util.Color;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class GameTest {
    Player player;
    Game game;
    Rules rules;
    EnemyRules enemyRules;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Before
    public void setUp() throws ParseException {
        String birthDate = "03/01/1998";
        Date dateOfBirth = dateFormat.parse(birthDate);
        player = new Player("John", dateOfBirth);
        game = new Game(player, 3 );
    }

    @After
    public void tearDown(){
        player = null;
        game = null;
    }

    @Test
    public void addPlayer_addPlayer_PlayersSorted() throws ParseException {
        String birthDate = "02/01/1998";
        Date dateOfBirth = dateFormat.parse(birthDate);
        Player player1 = new Player("Kate", dateOfBirth);
        String birthDate2 = "01/01/1998";
        Date dateOfBirth2 = dateFormat.parse(birthDate2);
        Player player2 = new Player("Julie", dateOfBirth2);
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
        Player player1 = new Player("Kate", dateOfBirth);
        String birthDate2 = "01/01/1998";
        Date dateOfBirth2 = dateFormat.parse(birthDate2);
        Player player2 = new Player("Julie", dateOfBirth2);
        String birthDate3 = "01/04/1998/";
        Date dateOfBirth3 = dateFormat.parse(birthDate3);
        Player player3 = new Player("Mary", dateOfBirth3);
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
    }*/ // TODO ALREADY CHECKED IN GET_NEXT_PLAYER

    @Test
    public void card_setUsedCards_getUsedCards(){
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
    public void isReady_game_readyToStart(){
        List<Card> usedCards = new ArrayList<>();
        Card card1 = new Card("Apollo", true, "Apollo", rules, enemyRules);
        Card card2 = new Card("Artemis", true, "Artemis", rules, enemyRules);
        Card card3 = new Card("Athena", true, "Athena", rules, enemyRules);
        usedCards.add(card1);
        usedCards.add(card2);
        usedCards.add(card3);
        game.setUsedCards(usedCards);
        Player player1 = new Player("Kate", new Date(1/5/1998));
        Player player2 = new Player("Julie", new Date(2/5/1998));
        game.addPlayer(player1);
        game.addPlayer(player2);
        assertTrue(game.isReady());
    }

    @Test
    public void isReady_game_notReadyToStart(){
        List<Card> usedCards = new ArrayList<>();
        game.setUsedCards(usedCards);
        Player player1 = new Player("Kate", new Date(1/5/1998));
        game.addPlayer(player1);
        assertFalse(game.isReady());
    }

    @Test
    public void getPlayersNicknames_game_getCorrectPlayersNicknames() throws ParseException {
        String birthDate = "02/01/1998";
        Date dateOfBirth = dateFormat.parse(birthDate);
        Player player1 = new Player("Kate", dateOfBirth);
        String birthDate2 = "01/01/1998";
        Date dateOfBirth2 = dateFormat.parse(birthDate2);
        Player player2 = new Player("Julie", dateOfBirth2);
        game.addPlayer(player1);
        game.addPlayer(player2);
        List<String> expectedNicknames= new ArrayList<>();
        expectedNicknames.add("John");
        expectedNicknames.add("Kate");
        expectedNicknames.add("Julie");
        assertEquals(expectedNicknames, game.getPlayersNickname());
    }

    @Test
    public void getPlayerByColor_game_getCorrectPlayerByColor() throws ParseException {
        String birthDate = "02/01/1998";
        Date dateOfBirth = dateFormat.parse(birthDate);
        Player player1 = new Player("Kate", dateOfBirth);
        String birthDate2 = "01/01/1998";
        Date dateOfBirth2 = dateFormat.parse(birthDate2);
        Player player2 = new Player("Julie", dateOfBirth2);
        game.addPlayer(player1);
        game.addPlayer(player2);
        player.chooseColor(Color.BLUE);
        player1.chooseColor(Color.PURPLE);
        assertEquals(player, game.getPlayerByColor(Color.BLUE));
        assertEquals(player1, game.getPlayerByColor(Color.PURPLE));
        assertNull(game.getPlayerByColor(Color.YELLOW));
        player2.chooseColor(Color.YELLOW);
        assertEquals(player2, game.getPlayerByColor(Color.YELLOW));
    }

    @Test
    public void getPlayerByNickname_game_getCorrectPlayerByNickname() throws ParseException {
        String birthDate = "02/01/1998";
        Date dateOfBirth = dateFormat.parse(birthDate);
        Player player1 = new Player("Kate", dateOfBirth);
        String birthDate2 = "01/01/1998";
        Date dateOfBirth2 = dateFormat.parse(birthDate2);
        Player player2 = new Player("Julie", dateOfBirth2);
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
        Player player1 = new Player("Kate", dateOfBirth);
        String birthDate2 = "01/01/1998";
        Date dateOfBirth2 = dateFormat.parse(birthDate2);
        Player player2 = new Player("Julie", dateOfBirth2);
        game.addPlayer(player1);
        game.addPlayer(player2);
        player1.setWinner(true);
        assertTrue(game.hasWinner());
    }

    @Test
    public void hasWinner_game_itHasNotAWinner() throws ParseException {
        String birthDate = "02/01/1998";
        Date dateOfBirth = dateFormat.parse(birthDate);
        Player player1 = new Player("Kate", dateOfBirth);
        String birthDate2 = "01/01/1998";
        Date dateOfBirth2 = dateFormat.parse(birthDate2);
        Player player2 = new Player("Julie", dateOfBirth2);
        game.addPlayer(player1);
        game.addPlayer(player2);
        assertFalse(game.hasWinner());
    }
}
