package Messages.ServerToClient;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static Util.MessageType.CV;
import static Util.MessageType.VC;
import static org.junit.Assert.assertEquals;

public class SetUpFirstPlayerTest {
    SetUpFirstPlayer setUpFirstPlayer1, setUpFirstPlayer2;
    String firstPlayerNickname;

    @Test
    public void setType_getCorrectType() {
        List<String> nicknames = new ArrayList<>();
        String nicknames1 = "John";
        String nicknames2 = "Joe";
        String nicknames3 = "Jack";
        nicknames.add(nicknames1);
        nicknames.add(nicknames2);
        nicknames.add(nicknames3);
        setUpFirstPlayer1 = new SetUpFirstPlayer(nicknames);
        setUpFirstPlayer2 = new SetUpFirstPlayer(firstPlayerNickname);
        assertEquals(CV, setUpFirstPlayer1.getType());
        assertEquals(VC, setUpFirstPlayer2.getType());
    }
}