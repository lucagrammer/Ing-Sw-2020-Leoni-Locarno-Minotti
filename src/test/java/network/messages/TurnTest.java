package network.messages;

import model.Game;
import org.junit.Before;
import org.junit.Test;
import util.*;

import static org.junit.Assert.assertEquals;
import static util.MessageType.CV;
import static util.MessageType.VC;

public class TurnTest {
    private Action action;
    private String nickname;
    private RoundActions roundActions;
    private Game game;
    private String loserNickname;

    @Before
    public void setUp() {
        game = new Game();
        action = new Action(ActionType.MOVE, Genre.FEMALE, Direction.S, 1);
        nickname = "Nick";
        roundActions = new RoundActions();
        roundActions.add(action);
        roundActions.add(new Action(ActionType.MOVE, Genre.MALE, Direction.S, 1));
        loserNickname = "Joe";
    }

    @Test
    public void setType_getCorrectType() {
        Turn turn1 = new Turn(action, nickname);
        Turn turn2 = new Turn(roundActions, game, loserNickname);
        assertEquals(CV, turn2.getType());
        assertEquals(VC, turn1.getType());
    }
}