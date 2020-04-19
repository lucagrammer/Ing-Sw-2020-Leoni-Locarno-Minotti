package Messages.ServerToClient;

import org.junit.Test;

import static Util.MessageType.MV;
import static org.junit.Assert.*;

public class WonGameMessageTest {
    WonGameMessage wonGameMessage;
    String winnerNickname;
    boolean youWin;

    @Test
    public void setType_getCorrectType() {
        wonGameMessage = new WonGameMessage(winnerNickname, youWin);
        assertEquals(MV, wonGameMessage.getType());
    }
}