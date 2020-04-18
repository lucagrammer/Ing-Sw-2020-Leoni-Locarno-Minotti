package Messages.ServerToClient;

import Model.Player;
import org.junit.Test;

import java.util.List;

import static Util.MessageType.MV;
import static org.junit.Assert.*;

public class ShowCardTest {
    ShowCard showCard;
    List<Player> playerList;

    @Test
    public void setType_getCorrectType() {
        showCard = new ShowCard(playerList);
        assertEquals(MV, showCard.getType());
    }
}