package network.messages;

import model.Game;
import model.Player;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static util.MessageType.CV;

public class ShowMapTest {
    private String currentNickname;
    private Game game;

    @Before
    public void setUp() {
        game = new Game();
        game.setNumPlayers(2);
        game.addPlayer(new Player("Joe", Date.from(Instant.now()), false));
        game.addPlayer(new Player("Rick", Date.from(Instant.now()), false));
        currentNickname = "Joe";
    }

    @Test
    public void setType_getCorrectType() {
        ShowMap showMap = new ShowMap(null, currentNickname, null);
        assertEquals(CV, showMap.getType());
    }
}