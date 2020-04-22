package Messages.ClientToServer;

import org.junit.Test;

import java.util.Date;

import static Util.MessageType.CV;
import static Util.MessageType.VC;
import static org.junit.Assert.assertEquals;

public class SetUpGameTest {
    SetUpGame setUpGame1, setUpGame2;
    String nickname;
    Date birthDate;
    int numPlayers;
    boolean newGame;

    @Test
    public void setType_getCorrectType() {
        setUpGame1 = new SetUpGame(nickname, birthDate, numPlayers);
        setUpGame2 = new SetUpGame(newGame);
        assertEquals(VC, setUpGame1.getType());
        assertEquals(CV, setUpGame2.getType());
    }

    @Test
    public void set_Nickname_getCorrectNickname() {
        setUpGame1 = new SetUpGame("Josh", birthDate, numPlayers);
        setUpGame2 = new SetUpGame(newGame);
        assertEquals("Josh", setUpGame1.getNickname());
    }

    @Test
    public void setNumPlayers_getCorrectNumPlayers() {
        setUpGame1 = new SetUpGame(nickname, birthDate, 3);
        setUpGame2 = new SetUpGame(newGame);
        assertEquals(3, setUpGame1.getNumPlayers());
    }

    @Test
    public void setBirthDate_getCorrectBirthDate() {
        setUpGame1 = new SetUpGame(nickname, new Date(12 / 06 / 1998), numPlayers);
        setUpGame2 = new SetUpGame(newGame);
        birthDate = new Date(12 / 06 / 1998);
        assertEquals(birthDate, setUpGame1.getBirthDate());
    }
}