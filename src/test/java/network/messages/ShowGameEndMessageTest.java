package network.messages;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static util.MessageType.MV;

public class ShowGameEndMessageTest {
    private String winnerNickname;
    private boolean youWin;

    @Before
    public void setUp() {
        youWin = true;
        winnerNickname = "Max";
    }

    @Test
    public void setType_getCorrectType() {
        ShowGameEndMessage showGameEndMessage = new ShowGameEndMessage(winnerNickname, youWin);
        assertEquals(MV, showGameEndMessage.getType());
    }
}