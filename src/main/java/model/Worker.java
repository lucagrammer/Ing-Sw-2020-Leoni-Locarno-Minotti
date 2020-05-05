package model;

import Util.Genre;
import Util.PlayerColor;

import java.io.Serializable;

/**
 * Stores information about a worker
 */
public class Worker implements Serializable {

    private final Genre genre;
    private final Player player;
    private PlayerColor playerColor;
    private Cell position;

    /**
     * Constructor: build a Worker
     *
     * @param genre  The genre of the worker
     * @param player The player who owns this worker
     */
    public Worker(Genre genre, Player player) {
        this.genre = genre;
        this.player = player;
        this.position = null;
        this.playerColor = null;
    }

    /**
     * Gets the current position of the worker
     *
     * @return The current position
     */
    public Cell getPosition() {
        return position;
    }

    /**
     * Sets the position of the worker
     *
     * @param position The new position
     */
    public void setPosition(Cell position) {
        this.position = position;
    }

    /**
     * Gets the playerColor of the worker
     *
     * @return The playerColor of the worker
     */
    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    /**
     * Sets the playerColor of the player
     *
     * @param playerColor The playerColor of the player
     */
    public void setPlayerColor(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    /**
     * Gets the genre of the worker
     *
     * @return The genre of the worker
     */
    public Genre getGenre() {
        return genre;
    }

    /**
     * Gets the player who owns this worker
     *
     * @return The player who owns this worker
     */
    public Player getPlayer() {
        return player;
    }
}
