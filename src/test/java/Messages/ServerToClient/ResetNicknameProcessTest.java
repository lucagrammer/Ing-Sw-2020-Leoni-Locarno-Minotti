package Messages.ServerToClient;

import org.junit.Test;

import static Util.MessageType.CV;
import static org.junit.Assert.*;

public class ResetNicknameProcessTest {
    ResetNicknameProcess resetNicknameProcess;
    String nickname;

    @Test
    public void setType_getCorrectType() {
        resetNicknameProcess= new ResetNicknameProcess(nickname);
        assertEquals(CV, resetNicknameProcess.getType());
    }

    @Test
    public void setNickname_getCorrectNickname() {
        resetNicknameProcess= new ResetNicknameProcess("John");
        assertEquals("John", resetNicknameProcess.getNickname());
    }
}