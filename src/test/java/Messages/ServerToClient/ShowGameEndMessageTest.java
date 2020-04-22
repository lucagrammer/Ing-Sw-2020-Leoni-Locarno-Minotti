package Messages.ServerToClient;

import org.junit.Test;

import static Util.MessageType.MV;
import static org.junit.Assert.assertEquals;

public class ShowGameEndMessageTest {
    ShowGameEndMessage showGameEndMessage;
    String winnerNickname;
    boolean youWin;

    @Test
    public void setType_getCorrectType() {
        showGameEndMessage = new ShowGameEndMessage(winnerNickname, youWin);
        assertEquals(MV, showGameEndMessage.getType());
    }
}