package util;

import model.Cell;
import model.Game;
import model.Player;
import model.Worker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A summary of the game map information
 */
public class MapInfo implements Serializable {
    private final String[][] cellColorMatrix;
    private final Genre[][] cellGenreMatrix;
    private final Cell[][] cellsInfoMatrix;
    private final List<String> playerNames;
    private final List<String> colors;
    private final List<String> cards;

    /**
     * Constructor: build a map info synthesis
     *
     * @param game The game
     */
    public MapInfo(Game game) {
        cellColorMatrix = new String[5][5];
        cellGenreMatrix = new Genre[5][5];
        cellsInfoMatrix = game.getBoard().getBoard();
        playerNames = new ArrayList<>();
        colors = new ArrayList<>();     // players color in the same order of playerNames
        cards = new ArrayList<>();      // players card in the same order of playerNames

        // Null initialization of the info matrix
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                cellColorMatrix[i][j] = null;
                cellGenreMatrix[i][j] = null;
            }
        }

        // Fill matrix with correct information
        for (Player player : game.getPlayers()) {
            for (Genre genre : Genre.values()) {
                Worker worker = player.getWorker(genre);
                Cell position = worker.getPosition();
                if (position != null) {
                    int row = position.getRow();
                    int column = position.getColumn();

                    cellColorMatrix[row][column] = worker.getPlayerColor().toString();
                    cellGenreMatrix[row][column] = genre;
                    if (!colors.contains(cellColorMatrix[row][column])) {
                        playerNames.add(player.getNickname());
                        cards.add(player.getCard().getName());
                        colors.add(cellColorMatrix[row][column]);
                    }
                }
            }
        }
    }

    /**
     * Gets the color of the worker positioned in a specific cell
     *
     * @param row    The row of the cell
     * @param column The column of the cell
     * @return A string containing the name of the color or null value if the cell is empty
     */
    public String getColorAt(int row, int column) {
        return cellColorMatrix[row][column] == null ? null : cellColorMatrix[row][column].toLowerCase();
    }

    /**
     * Checks if a specific cell has a dome
     *
     * @param row    The row of the cell
     * @param column The column of the cell
     * @return True if the cell has a dome, otherwise false
     */
    public boolean getDomeAt(int row, int column) {
        return cellsInfoMatrix[row][column].getDome();
    }

    /**
     * Gets the genre of the worker positioned in a specific cell
     *
     * @param row    The row of the cell
     * @param column The column of the cell
     * @return The genre of the worker or null value if the cell is empty
     */
    public Genre getGenreAt(int row, int column) {
        return cellGenreMatrix[row][column];
    }

    /**
     * Gets the floor number of a specific cell
     *
     * @param row    The row of the cell
     * @param column The column of the cell
     * @return The floor number
     */
    public int getFloorAt(int row, int column) {
        return cellsInfoMatrix[row][column].getFloor();
    }

    /**
     * Gets the number of the player positioned in the map
     *
     * @return The number of the player positioned in the map
     */
    public int getNumPlayers() {
        return playerNames.size();
    }

    /**
     * Gets a list containing the players positioned on the map sorted by turn
     *
     * @return a list containing the players positioned on the map sorted by turn
     */
    public List<String> getNicknames() {
        return new ArrayList<>(playerNames);
    }

    /**
     * Gets a list containing the colors of the players positioned on the map sorted by turn
     *
     * @return a list containing the colors of the players positioned on the map sorted by turn
     */
    public List<String> getColors() {
        return new ArrayList<>(colors);
    }

    /**
     * Gets a list containing the cards of the players positioned on the map sorted by turn
     *
     * @return a list containing the cards of the players positioned on the map sorted by turn
     */
    public List<String> getCards() {
        return new ArrayList<>(cards);
    }
}
