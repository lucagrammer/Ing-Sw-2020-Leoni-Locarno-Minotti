package Model;

import Util.Direction;
import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {
    Cell cell;
    Cell nextCell;
    Board board;
    Cell sCell;
    Cell neCell;

    @Test
    public void columnInfo_getCorrectColumn() {
        cell = new Cell (3, 2, board);
        assertEquals(2, cell.getColumn());
    }

    @Test
    public void rowInfo_getCorrectRow() {
        cell = new Cell (3, 2, board);
        assertEquals(3, cell.getRow());
    }

    @Test
    public void cellsInfo_isAdjacentCorrect() {
        cell = new Cell (3, 2, board);
        nextCell = new Cell(3, 3, board);
        assertTrue(cell.isAdjacent(nextCell));
    }


    @Test
    public void CalculateFloorDifference_getCorrectFloorDifference() {
        cell= new Cell(3,4, board);
        nextCell= new Cell(3, 3, board);
        cell.addFloor();
        assertEquals(-1, cell.getFloorDifference(nextCell));
        assertEquals(1, nextCell.getFloorDifference(cell));
    }


    @Test
    public void domeInfo_setDome_getCorrectValue() {
        cell= new Cell(3,4, board);
        cell.setDome(true);
        assertTrue(cell.getDome());
    }

    @Test
    public void domeInfo_setDome_GetCorrectIsCompleteTower() {
        cell= new Cell(3,4, board);
        cell.setDome(false);
        assertFalse(cell.isCompleteTower());
        cell.addFloor();
        cell.addFloor();
        cell.addFloor();
        cell.setDome(true);
        assertTrue(cell.isCompleteTower());
    }

    @Test
    public void floorInfo_AddFloor_GetCorrectFloor() {
        cell= new Cell(3,4, board);
        cell.addFloor();
        assertEquals(1, cell.getFloor());
        cell.addFloor();
        cell.addFloor();
        cell.addFloor();
        assertTrue(cell.getDome());
    }

    @Test
    public void cellsInfo_calculateCorrectDirection(){
        cell= new Cell(3, 3, board);
        assertEquals(Direction.N, cell.calculateDirection(new Cell (2, 3, board)));
        assertEquals(Direction.NE, cell.calculateDirection(new Cell(2,4, board)));
        assertEquals(Direction.E, cell.calculateDirection(new Cell (3, 4, board)));
        assertEquals(Direction.SE, cell.calculateDirection(new Cell( 4,4, board)));
        assertEquals(Direction.S, cell.calculateDirection(new Cell (4, 3, board)));
        assertEquals(Direction.SW, cell.calculateDirection(new Cell( 4,2, board)));
        assertEquals(Direction.W, cell.calculateDirection(new Cell (3, 2, board)));
        assertEquals(Direction.NW, cell.calculateDirection(new Cell( 2,2, board)));

    }
}


