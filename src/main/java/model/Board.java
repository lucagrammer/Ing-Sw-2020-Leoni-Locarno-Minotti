package model;

import util.Direction;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static util.Direction.*;

/**
 * Stores information about the game board
 */
public class Board implements Serializable {

    private static final int ROWS = 5;
    private static final int COLUMNS = 5;
    private final Cell[][] board;

    /**
     * Constructor: build the Board
     */
    public Board() {
        this.board = new Cell[ROWS][COLUMNS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                board[i][j] = new Cell(i, j);
            }
        }
    }

    /**
     * Gets the board as matrix
     *
     * @return The matrix of cells
     */
    public Cell[][] getBoard() {
        Cell[][] theBoard = new Cell[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(board[i], 0, theBoard[i], 0, COLUMNS);
        }
        return theBoard;
    }

    /**
     * Gets a specified Cell
     *
     * @param row    The row number
     * @param column The column number
     * @return The cell placed in specified coordinates or null value if row and column are not valid
     */
    public Cell getCell(int row, int column) {
        if (row >= 0 && row < ROWS && column >= 0 && column < COLUMNS)
            return board[row][column];
        else
            return null;
    }

    /**
     * Gets all the cells that are adjacent to a specified cell
     *
     * @param cell A cell of the board
     * @return A list of the adjacent cells
     */
    public List<Cell> getAdjacents(Cell cell) {
        return Stream.of(Direction.values()).map(direction -> getNextCell(cell, direction)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Gets the cell obtained by moving from one cell in one direction
     *
     * @param cell      The starting cell
     * @param direction The direction of the movement
     * @return The next cell
     */
    public Cell getNextCell(Cell cell, Direction direction) {
        Direction[][] possibleDirections = {{NW, N, NE}, {W, null, E}, {SW, S, SE}};

        for (int rowInc = 0; rowInc < 3; rowInc++) {
            for (int columnInc = 0; columnInc < 3; columnInc++) {
                if (possibleDirections[rowInc][columnInc] == direction) {
                    return getCell(cell.getRow() + (rowInc - 1), cell.getColumn() + (columnInc - 1));
                }
            }
        }
        return null;
    }

    /**
     * Gets the cell from which you moved to reach the current cell
     *
     * @param currentCell         The current cell
     * @param previouslyDirection The direction of the movement
     * @return The cell from which you moved or null value
     */
    public Cell getPrevCell(Cell currentCell, Direction previouslyDirection) {
        List<Cell> cells = getAdjacents(currentCell).
                stream().
                filter(cell -> currentCell.equals(getNextCell(cell, previouslyDirection))).
                collect(Collectors.toList());
        if (cells.size() > 0)
            return cells.get(0);
        else
            return null;
    }
}
