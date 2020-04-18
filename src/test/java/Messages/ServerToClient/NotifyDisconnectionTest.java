package Messages.ServerToClient;

import Util.MessageType;
import org.junit.Test;

import static Util.MessageType.MV;
import static org.junit.Assert.*;

public class NotifyDisconnectionTest {
    static NotifyDisconnection notifyDisconnection;

    @Test
    public void setType_getCorrectType() {
        String nickname= "John";
        notifyDisconnection= new NotifyDisconnection(nickname);
        MessageType messageType= MV;
        assertEquals(messageType, notifyDisconnection.getType());
    }
}