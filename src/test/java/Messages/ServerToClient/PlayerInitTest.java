package Messages.ServerToClient;

import Model.Cell;
import Model.Game;
import Util.MessageType;
import org.junit.Test;

import java.util.List;

import static Util.MessageType.CV;
import static Util.MessageType.VC;
import static org.junit.Assert.*;

public class PlayerInitTest {
    static  PlayerInit playerInit1, playerInit2;
    Game game;
    List<String> availableColors;
    String color, nickname;
    Cell malePosition, femalePosition;

    @Test
    public void setType_getCorrectType() {
        playerInit1= new PlayerInit(game, availableColors);
        playerInit2= new PlayerInit(color, malePosition, femalePosition, nickname);
        assertEquals(playerInit1.getType(), CV);
        assertEquals(playerInit2.getType(), VC);
    }
}