package network.messages;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static util.MessageType.CV;

public class SetUpNewNicknameTest {
    private SetUpNewNickname setUpNewNickname;
    private String temporaryNickname;

    @Before
    public void setUp() {
        temporaryNickname = "Joe";
    }

    @Test
    public void setType_getCorrectType() {
        setUpNewNickname = new SetUpNewNickname(temporaryNickname);
        assertEquals(CV, setUpNewNickname.getType());
    }

    @Test
    public void setNickname_getCorrectNickname() {
        setUpNewNickname = new SetUpNewNickname("John");
        assertEquals("John", setUpNewNickname.getTemporaryNickname());
    }
}