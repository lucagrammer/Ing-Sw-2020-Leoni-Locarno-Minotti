package Messages.ServerToClient;

import Model.Player;
import org.junit.Test;

import java.util.List;

import static Util.MessageType.MV;
import static org.junit.Assert.assertEquals;

public class ShowCardAssignmentTest {
    ShowCardAssignment showCardAssignment;
    List<Player> playerList;

    @Test
    public void setType_getCorrectType() {
        showCardAssignment = new ShowCardAssignment(playerList);
        assertEquals(MV, showCardAssignment.getType());
    }
}