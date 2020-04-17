package Messages.ServerToClient;

import Client.View;
import Messages.CVMessage;
import Messages.VCMessage;
import Model.Cell;
import Model.Game;
import Server.VirtualView;
import Util.MessageType;

import java.io.Serializable;
import java.util.List;

import static Util.Genre.FEMALE;
import static Util.Genre.MALE;

/**
 * Message for the choice of the color and the position of the workers of the player
 */
public class PlayerInit implements CVMessage, VCMessage, Serializable {
    MessageType messageType;
    Game game;
    List<String> availableColors;
    String color;
    Cell malePosition;
    Cell femalePosition;
    String nickname;

    /**
     * Server-side constructor: build a request message
     *
     * @param game            The game
     * @param availableColors The available colors
     */
    public PlayerInit(Game game, List<String> availableColors) {
        this.messageType = MessageType.CV;
        this.game = game;
        this.availableColors = availableColors;
    }

    /**
     * Client-side constructor: build a response message
     *
     * @param color          The chosen color
     * @param malePosition   The position of the male
     * @param femalePosition The position of the male
     * @param nickname       The nickname of the player
     */
    public PlayerInit(String color, Cell malePosition, Cell femalePosition, String nickname) {
        this.messageType = MessageType.VC;
        this.malePosition = malePosition;
        this.femalePosition = femalePosition;
        this.color = color;
        this.nickname = nickname;
    }

    /**
     * Gets the message type
     *
     * @return The message type
     */
    public MessageType getType() {
        return messageType;
    }

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    public void execute(View view) {
        view.chooseColorAndPosition(availableColors, game);
    }

    /**
     * Execute the request server-side
     *
     * @param virtualView The recipient component
     */
    public void execute(VirtualView virtualView) {
        virtualView.setColor(nickname, color);
        virtualView.setPosition(nickname, MALE, malePosition);
        virtualView.setPosition(nickname, FEMALE, femalePosition);
    }
}
