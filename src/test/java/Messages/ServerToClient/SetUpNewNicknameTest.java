package Messages.ServerToClient;

import org.junit.Test;

import static Util.MessageType.CV;
import static org.junit.Assert.assertEquals;

public class SetUpNewNicknameTest {
    SetUpNewNickname setUpNewNickname;
    String nickname;

    @Test
    public void setType_getCorrectType() {
        setUpNewNickname = new SetUpNewNickname(nickname);
        assertEquals(CV, setUpNewNickname.getType());
    }

    @Test
    public void setNickname_getCorrectNickname() {
        setUpNewNickname = new SetUpNewNickname("John");
        assertEquals("John", setUpNewNickname.getNickname());
    }
}