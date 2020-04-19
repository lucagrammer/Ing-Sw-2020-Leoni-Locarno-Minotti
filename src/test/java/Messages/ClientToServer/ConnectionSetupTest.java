package Messages.ClientToServer;

import org.junit.Test;

import java.util.Date;

import static Util.MessageType.CV;
import static Util.MessageType.VC;
import static org.junit.Assert.*;

public class ConnectionSetupTest {
    ConnectionSetup connectionSetup1, connectionSetup2;
    String nickname;
    Date birthDate;
    int numPlayers;
    boolean newGame;

    @Test
    public void setType_getCorrectType() {
        connectionSetup1 = new ConnectionSetup(nickname, birthDate, numPlayers);
        connectionSetup2= new ConnectionSetup(newGame);
        assertEquals(VC, connectionSetup1.getType());
        assertEquals(CV, connectionSetup2.getType());
    }

    @Test
    public void set_Nickname_getCorrectNickname() {
        connectionSetup1 = new ConnectionSetup("Josh", birthDate, numPlayers);
        connectionSetup2 = new ConnectionSetup(newGame);
        assertEquals("Josh", connectionSetup1.getNickname());
    }

    @Test
    public void setNumPlayers_getCorrectNumPlayers() {
        connectionSetup1= new ConnectionSetup(nickname, birthDate, 3);
        connectionSetup2= new ConnectionSetup(newGame);
        assertEquals(3, connectionSetup1.getNumPlayers());
    }

    @Test
    public void setBirthDate_getCorrectBirthDate() {
        connectionSetup1= new ConnectionSetup(nickname, new Date(12/06/1998), numPlayers);
        connectionSetup2= new ConnectionSetup(newGame);
        birthDate= new Date(12/06/1998);
        assertEquals(birthDate,connectionSetup1.getBirthDate());
    }
}