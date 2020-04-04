package Model;

import Util.Direction;

/**
 * Stores information about a cell of the game board
 */
public class Cell {

    private int row;
    private int column;
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
     * @return The column number of the cell
     */
    public int getColumn() {
        return column;
    }

    /**
     * Gets the row number
     * @return The row number
     */
    public int getRow() {
        return row;
    }

    /**
     * Checks if the next cell is adjacent to the current one
     * @param nextCell  The cell to be checked
     * @return True is the cell is adjacent to the current one
     */
    public boolean isAdjacent(Cell nextCell){
        int nextRow=nextCell.getRow();
        int nextColumn=nextCell.getColumn();

        return Math.abs(nextRow - row)<=1 && Math.abs(nextColumn - column)<=1 && (nextRow!=row || nextColumn!=column);
    }

    /**
     * Gets the level difference between the current cell and a specified cell
     * @param nextCell  The specified cell
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
        if(this.floor<3){
            floor++;
        }
        else {
            dome = true;
        }
    }

    /**
     * Gets the number of floors
     * @return The number of the floors
     */
    public int getFloor() {
        return floor;
    }

    /**
     * Calculates the direction in which you need to move to reach a specified cell
     * @param adjacentCell  The cell to be reached
     * @return The direction in which you need to move
     */
    public Direction calculateDirection(Cell adjacentCell) {
        int rowDifference = adjacentCell.getRow() - row;
        int columnDifference = adjacentCell.getColumn() - column;

        switch (rowDifference) {
            case 0:
                switch (columnDifference) {
                    case -1:
                        return Direction.W;
                    case 1:
                        return Direction.E;
                }
            case -1:
                switch (columnDifference) {
                    case 0:
                        return Direction.N;
                    case -1:
                        return Direction.NW;
                    case 1:
                        return Direction.NE;
                }
            default:
                switch (columnDifference) {
                    case 0:
                        return Direction.S;
                    case -1:
                        return Direction.SW;
                    default:
                        return Direction.SE;
                }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row &&
                column == cell.column &&
                dome == cell.dome &&
                floor == cell.floor;
    }
}
