package network.messages;

import client.View;
import network.CVMessage;
import network.SYSMessage;
import server.VirtualView;
import util.MessageType;

import java.io.Serializable;
import java.util.Date;

/**
 * Server connection message and exchange of player and game information
 */
public class SetUpGame implements Serializable, CVMessage, SYSMessage {
    private final MessageType messageType;
    private String nickname;
    private String temporaryNickname;
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
    public SetUpGame(String nickname, Date birthDate, int numPlayers) {
        this.messageType = MessageType.SYS;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.numPlayers = numPlayers;
    }

    /**
     * Server-side constructor: build the request
     *
     * @param newGame           True if the player has to decide the number of players, otherwise false
     * @param temporaryNickname The temporary nickname
     */
    public SetUpGame(boolean newGame, String temporaryNickname) {
        this.messageType = MessageType.CV;
        this.newGame = newGame;
        this.temporaryNickname = temporaryNickname;
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
     * Gets the temporary nickname of the user
     *
     * @return The temporary nickname
     */
    public String getTemporaryNickname() {
        return temporaryNickname;
    }

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    public void execute(View view) {
        view.setUpGame(newGame);
    }

    /**
     * Execute the request server-side
     *
     * @param virtualView       The recipient component
     * @param temporaryUsername The temporary username of the user
     */
    public void execute(VirtualView virtualView, String temporaryUsername) {
        virtualView.setUpGame(temporaryUsername, nickname, birthDate, numPlayers);
    }
}
