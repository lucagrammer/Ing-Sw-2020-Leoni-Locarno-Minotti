package Model;

import Server.Rules.EnemyRules;
import Server.Rules.Rules;
import Util.Color;
import Util.Genre;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

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
}