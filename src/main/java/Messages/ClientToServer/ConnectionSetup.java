package Messages.ClientToServer;

import Client.View;
import Messages.CVMessage;
import Messages.VCMessage;
import Server.VirtualView;
import Util.MessageType;

import java.io.Serializable;
import java.util.Date;

/**
 * Server connection message and exchange of player and game information
 */
public class ConnectionSetup implements Serializable, CVMessage, VCMessage {
    private final MessageType messageType;
    private String nickname;
    private Date birthDate;
    private int numPlayers;
    private boolean newGame;

    /**
     * Client-side constructor: build the response message
     *
     * @param nickname   The chosen nickname
     * @param birthDate  The birth date of the player
     * @param numPlayers The number of the players for the game
     */
    public ConnectionSetup(String nickname, Date birthDate, int numPlayers) {
        this.messageType = MessageType.VC;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.numPlayers = numPlayers;
    }

    /**
     * Server-side constructor: build the request
     *
     * @param newGame True if the player has to decide the number of players, otherwise false
     */
    public ConnectionSetup(boolean newGame) {
        this.messageType = MessageType.CV;
        this.newGame = newGame;
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
     * Gets the chosen nickname
     *
     * @return The chosen nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Gets the number of players
     *
     * @return The number of players
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Gets the birth date
     *
     * @return The birth date
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    public void execute(View view) {
        view.gameSetUp(newGame);
    }

    /**
     * Execute the request server-side
     *
     * @param virtualView The recipient component
     */
    public void execute(VirtualView virtualView) {
        // specific management of the request assigned to the server
    }
}
