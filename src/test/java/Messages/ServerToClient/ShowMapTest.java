package Messages.ServerToClient;

import Model.Game;
import org.junit.Test;

import static Util.MessageType.CV;
import static org.junit.Assert.assertEquals;

public class ShowMapTest {
    ShowMap showMap;
    String currentNickname;
    Game game;

    @Test
    public void setType_getCorrectType() {
        showMap = new ShowMap(game, currentNickname, null);
        assertEquals(CV, showMap.getType());
    }
}