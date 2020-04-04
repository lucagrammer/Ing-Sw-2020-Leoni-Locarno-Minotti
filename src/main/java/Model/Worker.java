package Model;

import Util.Color;
import Util.Genre;

/**
 * Stores information about a worker
 */
public class Worker {

    private Color color;
    private Genre genre;
    private Player player;
    private Cell position;

    /**
     * Constructor: build a Worker
     *
     * @param genre  The genre of the worker
     * @param player The player who owns this worker
     */
    public Worker(Genre genre, Color color, Player player) {
        this.genre = genre;
        this.color = color;
        this.player = player;
        this.position=null;
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
     * Gets the genre of the worker
     * @return The genre of the worker
     */
    public Genre getGenre() {
        return genre;
    }

    /**
     * Gets the player who owns this worker
     * @return The player who owns this worker
     */
    public Player getPlayer() {
        return player;
    }
}
