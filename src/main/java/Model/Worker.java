package Model;

import Util.Color;
import Util.Genre;

public class Worker {

    private Color color;
    private Genre genre;
    private Player player;
    private Cell position;

    /**
     * Build a Worker
     * @param genre the genre of the worker
     * @param player the player who owns this worker
     */
    public Worker(Genre genre, Player player) {
        this.genre = genre;
        this.player = player;
        this.position=null;
    }

    /**
     * Sets the position of the worker
     * @param position the new position
     */
    public void setPosition(Cell position) {
        this.position = position;
    }

    /**
     * Gets the current position of the worker
     * @return the current position
     */
    public Cell getPosition() {
        return position;
    }

    /**
     * Sets the color of the worker
     * @param color the color of the worker
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gets the color of the worker
     * @return the color of the worker
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gets the genre of the worker
     * @return the genre of the worker
     */
    public Genre getGenre() {
        return genre;
    }

    /**
     * Gets the player who owns this worker
     * @return the player who owns this worker
     */
    public Player getPlayer() {
        return player;
    }
}
