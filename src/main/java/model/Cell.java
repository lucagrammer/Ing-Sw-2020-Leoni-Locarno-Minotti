package model;

import util.Direction;

import java.io.Serializable;

/**
 * Stores information about a cell of the game board
 */
public class Cell implements Serializable {

    private final int row;
    private final int column;
    private boolean dome;
    private int floor;

    /**
     * Constructor: build a cell
     *
     * @param row    The row number of the cell
     * @param column The the column number of the cell
     */
    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        this.dome = false;
        this.floor = 0;
    }

    /**
     * Gets the column number of the cell
     *
     * @return The column number of the cell
     */
    public int getColumn() {
        return column;
    }

    /**
     * Gets the row number
     *
     * @return The row number
     */
    public int getRow() {
        return row;
    }

    /**
     * Checks if the next cell is adjacent to the current one
     *
     * @param nextCell The cell to be checked
     * @return True is the cell is adjacent to the current one
     */
    public boolean isAdjacent(Cell nextCell) {
        int nextRow = nextCell.getRow();
        int nextColumn = nextCell.getColumn();

        return Math.abs(nextRow - row) <= 1 && Math.abs(nextColumn - column) <= 1 && (nextRow != row || nextColumn != column);
    }

    /**
     * Gets the level difference between the current cell and a specified cell
     *
     * @param nextCell The specified cell
     * @return The difference (positive or negative) between the current cell and a specified cell
     */
    public int getFloorDifference(Cell nextCell) {
        return nextCell.getFloor() - this.getFloor();
    }

    /**
     * Gets the dome attribute
     *
     * @return True if the building has a dome, otherwise false
     */
    public boolean getDome() {
        return dome;
    }

    /**
     * Sets the dome attribute
     *
     * @param set True to add the dome, false to remove the dome
     */
    public void setDome(boolean set) {
        dome = set;
    }

    /**
     * Tests if the tower is complete
     *
     * @return True if the tower is complete, otherwise false
     */
    public boolean isCompleteTower() {
        return dome && (floor == 3);
    }

    /**
     * Build a floor or if the are three floors build the dome
     */
    public void addFloor() {
        if (this.floor < 3) {
            floor++;
        } else {
            dome = true;
        }
    }

    /**
     * Gets the number of floors
     *
     * @return The number of the floors
     */
    public int getFloor() {
        return floor;
    }

    /**
     * Calculates the direction in which you need to move to reach a specified cell
     *
     * @param adjacentCell The cell to be reached
     * @return The direction in which you need to move
     */
    public Direction calculateDirection(Cell adjacentCell) {
        int rowDifference = adjacentCell.getRow() - row;
        int columnDifference = adjacentCell.getColumn() - column;
        if(Math.abs(rowDifference)<2 && Math.abs(columnDifference)<2 && !(rowDifference==0 && columnDifference==0)) {
            return Direction.fromDiff(rowDifference, columnDifference);
        }else{
            return null;
        }
    }

    /**
     * Tests if the current object is equal to another object
     *
     * @param obj The other object
     * @return True if the objects are the same
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cell cell = (Cell) obj;
        return row == cell.row &&
                column == cell.column;
    }

    /**
     * Gets the cell info
     *
     * @return A string containing cell info
     */
    public String toString() {
        return "row=" + row +
                ", column=" + column +
                ", dome=" + dome +
                ", floor=" + floor;
    }
}
