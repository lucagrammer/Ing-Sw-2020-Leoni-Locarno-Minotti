package network.messages;

import client.View;
import model.Cell;
import network.CVMessage;
import network.VCMessage;
import server.VirtualView;
import util.Genre;
import util.MapInfo;
import util.MessageType;

import java.io.Serializable;
import java.util.List;

/**
 * Message for the choice of the color and the position of the workers of the player
 */
public class SetUpPlayerPosition implements CVMessage, VCMessage, Serializable {
    private final MessageType messageType;
    private final Genre genre;
    private MapInfo mapInfo;
    private List<Cell> forbiddenCells;
    private Cell chosenPosition;
    private String nickname;

    /**
     * Server-side constructor: build a request message
     *
     * @param genre          The genre of the worker
     * @param forbiddenCells The forbidden cells
     * @param mapInfo        The map info
     */
    public SetUpPlayerPosition(Genre genre, List<Cell> forbiddenCells, MapInfo mapInfo) {
        this.messageType = MessageType.CV;
        this.mapInfo=mapInfo;
        this.genre = genre;
        this.forbiddenCells = forbiddenCells;
    }

    /**
     * Client-side constructor: build a response message
     *
     * @param genre          The genre of the worker
     * @param chosenPosition The chosen position of the worker
     * @param nickname       The nickname of the player
     */
    public SetUpPlayerPosition(Genre genre, Cell chosenPosition, String nickname) {
        this.messageType = MessageType.VC;
        this.chosenPosition = chosenPosition;
        this.genre = genre;
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
        view.askPlayerPosition(genre, forbiddenCells, mapInfo);
    }

    /**
     * Execute the request server-side
     *
     * @param virtualView The recipient component
     */
    public void execute(VirtualView virtualView) {
        virtualView.setPlayerPosition(nickname, genre, chosenPosition);
    }
}
