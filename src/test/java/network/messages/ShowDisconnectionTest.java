package network.messages;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static util.MessageType.MV;

public class ShowDisconnectionTest {

    @Test
    public void setType_getCorrectType() {
        String nickname = "John";
        ShowDisconnection showDisconnection = new ShowDisconnection(nickname);
        assertEquals(MV, showDisconnection.getType());
    }
}