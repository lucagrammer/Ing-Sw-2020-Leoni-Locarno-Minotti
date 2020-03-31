package Model;

public class Board {

    private static final int ROWS = 5;
    private static final int COLUMNS = 5;
    private Cell [][] board;
    private Game game;

    /**
     * Build the Board
     */
    public Board(Game game){
        this.game=game;
        this.board= new Cell [ROWS][COLUMNS];

        for(int i=0; i<ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                board[i][j] = new Cell(i, j, game);
            }
        }
    }

    /**
     * Gets a specified Cell
     * @param row is the row number
     * @param column is the column number
     * @return the cell placed in specified coordinates
     */
    public Cell getCell(int row, int column) {
        return board [row][column];
    }
}
