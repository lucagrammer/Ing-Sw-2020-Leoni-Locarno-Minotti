package Messages.ServerToClient;

import Model.Card;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static Util.MessageType.CV;
import static Util.MessageType.VC;
import static org.junit.Assert.*;

public class SelectFirstTest {
    SelectFirst selectFirst1, selectFirst2;
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
        selectFirst1= new SelectFirst(nicknames);
        selectFirst2= new SelectFirst(firstPlayerNickname);
        assertEquals(CV, selectFirst1.getType());
        assertEquals(VC, selectFirst2.getType());
    }
}