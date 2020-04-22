package Model;

import Util.Color;
import Util.Genre;

import java.io.Serializable;

/**
 * Stores information about a worker
 */
public class Worker implements Serializable {

    private final Genre genre;
    private final Player player;
    private Color color;
    private Cell position;

    /**
     * Constructor: build a Worker
     *
     * @param genre  The genre of the worker
     * @param player The player who owns this worker
     */
    public Worker(Genre genre, Color color, Player player) {
        //TODO RIMUOVERE
        this.genre = genre;
        this.color = color;
        this.player = player;
        this.position = null;
    }

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
        this.color = null;
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
     * Gets the color of the worker
     *
     * @return The color of the worker
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of the player
     *
     * @param color The color of the player
     */
    public void setColor(Color color) {
        this.color = color;
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
