package Messages.ServerToClient;

import Util.MessageType;
import org.junit.Test;

import static Util.MessageType.MV;
import static org.junit.Assert.assertEquals;

public class ShowDisconnectionTest {
    static ShowDisconnection showDisconnection;

    @Test
    public void setType_getCorrectType() {
        String nickname = "John";
        showDisconnection = new ShowDisconnection(nickname);
        MessageType messageType = MV;
        assertEquals(messageType, showDisconnection.getType());
    }
}