package Model;

import Util.Direction;
import Util.Genre;

import java.util.Objects;

public class Cell {

    private int row;
    private int column;
    private boolean dome;
    private int floor;
    private Board board; // TODO: FORSE DA RIMUOVERE

    /**
     * Build a cell
     *
     * @param row    is row number
     * @param column is the column number
     */
    public Cell(int row, int column, Board board) {
        this.row = row;
        this.column = column;
        this.dome = false;
        this.floor = 0;
        this.board = board;
    }

    /**
     * Gets the column number
     * @return the column number
     */
    public int getColumn() {
        return column;
    }

    /**
     * Gets the row number
     * @return the row number
     */
    public int getRow() {
        return row;
    }

    /**
     * Checks if the next cell is adjacent to the current one
     * @param nextCell the cell to be checked
     * @return true is the cell is adjacent to the current one
     */
    public boolean isAdjacent(Cell nextCell){
        int nextRow=nextCell.getRow();
        int nextColumn=nextCell.getColumn();

        return Math.abs(nextRow - row)<=1 && Math.abs(nextColumn - column)<=1 && (nextRow!=row || nextColumn!=column);
    }

    /**
     * Tests if the cell is free
     * @return true if the cell is not occupied by a worker and doesn't have the couple
     */
    // TODO: FORSE DA RIMUOVERE, NON TESTARE
    public boolean isFree(){
        return getWorkerHere()==null && !this.getDome();
    }

    /**
     * Gets the worker that occupies the cell
     *
     * @return the worker who occupies the cell or the null value if the cell is not occupied
     */
    // TODO: FORSE DA RIMUOVERE, NON TESTARE
    public Worker getWorkerHere() {
        for (Player player : board.getGame().getPlayers()) {
            Worker femaleWorker = player.getWorker(Genre.FEMALE);
            if (femaleWorker.getPosition() == this) {
                return femaleWorker;
            } else {
                Worker maleWorker = player.getWorker(Genre.MALE);
                if (maleWorker.getPosition() == this)
                    return maleWorker;
            }
        }
        return null;
    }

    /**
     * Gets the level difference between the current cell and a specified cell
     *
     * @param nextCell the specified cell
     * @return the difference (positive or negative) between the current cell and a specified cell
     */
    public int getFloorDifference(Cell nextCell) {
        return nextCell.getFloor() - this.getFloor();
    }

    /**
     * Sets the dome attribute
     *
     * @param set true to add the dome, false to remove the dome
     */
    public void setDome(boolean set) {
        dome = set;
    }

    /**
     * Gets the dome attribute
     * @return true if the building has a dome, otherwise false
     */
    public boolean getDome() {
        return dome;
    }

    /**
     * Tests if the tower is complete
     * @return true if the tower is complete, otherwise false
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
     *
     * @return the number of the floors
     */
    public int getFloor() {
        return floor;
    }

    /**
     * Calculates the direction in which you need to move to reach a specified cell
     *
     * @param adjacentCell the cell to be reached
     * @return the direction in wich you need to move
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
