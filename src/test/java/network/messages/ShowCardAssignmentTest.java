package network.messages;

import model.Player;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static util.MessageType.MV;

public class ShowCardAssignmentTest {
    private List<Player> playerList;

    @Before
    public void setUp() {
        playerList = new ArrayList<>();
        playerList.add(new Player("Paul", Date.from(Instant.now()), false));
        playerList.add(new Player("Joe", Date.from(Instant.now()), false));
    }

    @Test
    public void setType_getCorrectType() {
        ShowCardAssignment showCardAssignment = new ShowCardAssignment(playerList);
        assertEquals(MV, showCardAssignment.getType());
    }
}