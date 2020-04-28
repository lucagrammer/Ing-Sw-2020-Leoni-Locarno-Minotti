package model;

import Util.Direction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BoardTest {

    Board board;
    Cell cell;

    @Test
    public void board_getCorrectBoard_getCorrectCell() {
        board = new Board();
        assertNull(board.getCell(4, 6));
        assertEquals(board.getBoard()[3][3], board.getCell(3, 3));
        assertNull(board.getCell(6, 6));
    }

    @Test
    public void getCorrectAdjacents() {
        board = new Board();
        List<Cell> cells = new ArrayList<>();
        cells.add(board.getCell(2, 3));
        cells.add(board.getCell(2, 4));
        cells.add(board.getCell(3, 4));
        cells.add(board.getCell(4, 4));
        cells.add(board.getCell(4, 3));
        cells.add(board.getCell(4, 2));
        cells.add(board.getCell(3, 2));
        cells.add(board.getCell(2, 2));
        assertEquals(cells, board.getAdjacents(board.getCell(3, 3)));
    }

    @Test
    public void getNextCell_board_getCorrectNextCell() {
        board = new Board();
        cell = board.getCell(3, 3);
        assertEquals(board.getCell(2,3), board.getNextCell(cell, Direction.N));
        assertEquals(board.getCell(3,4), board.getNextCell(cell, Direction.E));
        assertEquals(board.getCell(4,3), board.getNextCell(cell, Direction.S));
        assertEquals(board.getCell(3,2), board.getNextCell(cell, Direction.W));
        assertEquals(board.getCell(2,4), board.getNextCell(cell, Direction.NE));
        assertEquals(board.getCell(4,4), board.getNextCell(cell, Direction.SE));
        assertEquals(board.getCell(4,2), board.getNextCell(cell, Direction.SW));
        assertEquals(board.getCell(2,2), board.getNextCell(cell, Direction.NW));
        cell = board.getCell(4, 4);
        assertNull(board.getNextCell(cell, Direction.SE));
    }

}