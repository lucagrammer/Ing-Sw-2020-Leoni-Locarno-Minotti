package util;

import model.Cell;
import model.Game;
import model.Player;
import model.Worker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MapInfo implements Serializable {
    private final String[][] cellColorMatrix;
    private final Genre[][] cellGenreMatrix;
    private final Cell[][] cellsInfoMatrix;
    private final List<String> playerNames;
    private final List<String> colors;
    private final List<String> cards;

    public MapInfo(Game game){
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
            cards.add(player.getCard().getName());
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
                        colors.add(cellColorMatrix[row][column]);
                    }
                }
            }
        }
    }

    public String getColorAt(int row,int column){
        return cellColorMatrix[row][column]==null? null : cellColorMatrix[row][column].toLowerCase();
    }

    public boolean getDomeAt(int row,int column){
        return cellsInfoMatrix[row][column].getDome();
    }

    public Genre getGenreAt(int row,int column){
        return cellGenreMatrix[row][column];
    }

    public int getFloorAt(int row,int column){
        return cellsInfoMatrix[row][column].getFloor();
    }

    public int getNumPlayers(){
        return playerNames.size();
    }

    public List<String> getNicknames(){
        return new ArrayList<>(playerNames);
    }

    public List<String> getColors(){
        return new ArrayList<>(colors);
    }

    public List<String> getCards(){
        return new ArrayList<>(cards);
    }
}
