package network.messages;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static util.MessageType.CV;
import static util.MessageType.SYS;

public class SetUpGameTest {
    private String nickname;
    private Date birthDate;
    private int numPlayers;
    private boolean newGame;

    @Before
    public void setUp() {
        nickname = "Joe";
        birthDate = Date.from(Instant.now());
        numPlayers = 2;
        newGame = true;
    }

    @Test
    public void setType_getCorrectType() {
        SetUpGame setUpGame1 = new SetUpGame(nickname, birthDate, numPlayers);
        SetUpGame setUpGame2 = new SetUpGame(newGame, "");
        assertEquals(SYS, setUpGame1.getType());
        assertEquals(CV, setUpGame2.getType());
    }
}