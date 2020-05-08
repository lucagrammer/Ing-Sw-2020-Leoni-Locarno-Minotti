package network.messages;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static util.MessageType.CV;
import static util.MessageType.VC;

public class SetUpFirstPlayerTest {
    private String firstPlayerNickname;

    @Before
    public void setUp() {
        firstPlayerNickname = "Joe";
    }

    @Test
    public void setType_getCorrectType() {
        List<String> nicknames = new ArrayList<>();
        String nickname2 = "Joe";
        String nickname3 = "Jack";
        nicknames.add(firstPlayerNickname);
        nicknames.add(nickname2);
        nicknames.add(nickname3);
        SetUpFirstPlayer setUpFirstPlayer1 = new SetUpFirstPlayer(nicknames);
        SetUpFirstPlayer setUpFirstPlayer2 = new SetUpFirstPlayer(firstPlayerNickname);
        assertEquals(CV, setUpFirstPlayer1.getType());
        assertEquals(VC, setUpFirstPlayer2.getType());
    }
}