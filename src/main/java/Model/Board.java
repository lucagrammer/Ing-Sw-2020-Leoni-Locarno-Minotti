package Model;

import Util.Direction;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {

    private static final int ROWS = 5;
    private static final int COLUMNS = 5;
    private Cell[][] board;
    private Game game; // TODO: FORSE DA RIMUOVERE

    /**
     * Build the Board
     */
    public Board(Game game) {
        this.game = game;
        this.board = new Cell[ROWS][COLUMNS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                board[i][j] = new Cell(i, j, this);
            }
        }
    }

    public Cell[][] getBoard(){
        return board;
    }

    /**
     * Gets a specified Cell
     *
     * @param row    is the row number
     * @param column is the column number
     * @return the cell placed in specified coordinates or null value if row and column are not valid
     */
    public Cell getCell(int row, int column) {
        if (row >= 0 && row < ROWS && column >= 0 && column < COLUMNS)
            return board[row][column];
        else
            return null;
    }

    public List<Cell> getAdjacents(Cell cell) {
        return Stream.of(Direction.values()).map(direction -> getNextCell(cell, direction)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public Cell getNextCell(Cell cell, Direction direction) {
        Cell nextCell = null;

        switch (direction) {
            case N:
                nextCell = getCell(cell.getRow() - 1, cell.getColumn());
                break;
            case NE:
                nextCell = getCell(cell.getRow() - 1, cell.getColumn() + 1);
                break;
            case E:
                nextCell = getCell(cell.getRow(), cell.getColumn() + 1);
                break;
            case SE:
                nextCell = getCell(cell.getRow() + 1, cell.getColumn() + 1);
                break;
            case S:
                nextCell = getCell(cell.getRow() + 1, cell.getColumn());
                break;
            case SW:
                nextCell = getCell(cell.getRow() + 1, cell.getColumn() - 1);
                break;
            case W:
                nextCell = getCell(cell.getRow(), cell.getColumn() - 1);
                break;
            case NW:
                nextCell = getCell(cell.getRow() - 1, cell.getColumn() - 1);
                break;
        }

        return nextCell;
    }

    /**
     * Gets the game that uses the Board
     *
     * @return the game that uses the board
     */
    // TODO: FORSE DA RIMUOVERE, NON TESTARE
    public Game getGame() {
        return game;
    }

    /**
     * Gets the cell from which you moved to reach the current cell
     *
     * @param currentCell             the current cell
     * @param previouslyMoveDirection the direction of the movement
     * @return the cell from which you moved or null value
     */
    public Cell getPrevCell(Cell currentCell, Direction previouslyMoveDirection) {
        List<Cell> cells = getAdjacents(currentCell).
                stream().
                filter(cell -> currentCell.equals(getNextCell(cell, previouslyMoveDirection))).
                collect(Collectors.toList());
        if (cells.size() > 0)
            return cells.get(0);
        else
            return null;
    }
}
