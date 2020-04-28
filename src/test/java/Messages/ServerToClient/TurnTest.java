package Messages.ServerToClient;

import model.Game;
import Util.Action;
import Util.RoundActions;
import org.junit.Test;

import static Util.MessageType.CV;
import static Util.MessageType.VC;
import static org.junit.Assert.assertEquals;

public class TurnTest {
    Turn turn1, turn2;
    Action action;
    String nickname;
    RoundActions roundActions;
    Game game;

    @Test
    public void setType_getCorrectType() {
        turn1 = new Turn(action, nickname);
        turn2 = new Turn(roundActions, game, null);
        assertEquals(CV, turn2.getType());
        assertEquals(VC, turn1.getType());
    }
}