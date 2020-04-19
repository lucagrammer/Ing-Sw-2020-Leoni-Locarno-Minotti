package Messages.ServerToClient;

import Util.Action;
import Util.RoundActions;
import org.junit.Test;

import java.util.ArrayList;

import static Util.MessageType.CV;
import static Util.MessageType.VC;
import static org.junit.Assert.*;

public class TurnTest {
    Turn turn1, turn2;
    Action action;
    String nickname;
    RoundActions roundActions;

    @Test
    public void setType_getCorrectType() {
        turn1= new Turn(action, nickname);
        turn2= new Turn(roundActions);
        assertEquals(CV, turn2.getType());
        assertEquals(VC, turn1.getType());
    }
}