package model;

import org.junit.Test;
import util.Direction;

import static org.junit.Assert.*;

public class CellTest {
    private Cell cell;
    private Cell nextCell;

    @Test
    public void columnInfo_getCorrectColumn() {
        cell = new Cell(3, 2);
        assertEquals(2, cell.getColumn());
    }

    @Test
    public void rowInfo_getCorrectRow() {
        cell = new Cell(3, 2);
        assertEquals(3, cell.getRow());
    }

    @Test
    public void cellsInfo_isAdjacentCorrect() {
        cell = new Cell(3, 2);
        nextCell = new Cell(3, 3);
        assertTrue(cell.isAdjacent(nextCell));
    }

    @Test
    public void CalculateFloorDifference_getCorrectFloorDifference() {
        cell = new Cell(3, 4);
        nextCell = new Cell(3, 3);
        cell.addFloor();
        assertEquals(-1, cell.getFloorDifference(nextCell));
        assertEquals(1, nextCell.getFloorDifference(cell));
    }

    @Test
    public void domeInfo_setDome_getCorrectValue() {
        cell = new Cell(3, 4);
        cell.setDome(true);
        assertTrue(cell.getDome());
    }

    @Test
    public void domeInfo_setDome_GetCorrectIsCompleteTower() {
        cell = new Cell(3, 4);
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
        cell = new Cell(3, 4);
        cell.addFloor();
        assertEquals(1, cell.getFloor());
        cell.addFloor();
        cell.addFloor();
        cell.addFloor();
        assertTrue(cell.getDome());
    }

    @Test
    public void cellsInfo_calculateCorrectDirection() {
        cell = new Cell(3, 3);
        assertEquals(Direction.N, cell.calculateDirection(new Cell(2, 3)));
        assertEquals(Direction.NE, cell.calculateDirection(new Cell(2, 4)));
        assertEquals(Direction.E, cell.calculateDirection(new Cell(3, 4)));
        assertEquals(Direction.SE, cell.calculateDirection(new Cell(4, 4)));
        assertEquals(Direction.S, cell.calculateDirection(new Cell(4, 3)));
        assertEquals(Direction.SW, cell.calculateDirection(new Cell(4, 2)));
        assertEquals(Direction.W, cell.calculateDirection(new Cell(3, 2)));
        assertEquals(Direction.NW, cell.calculateDirection(new Cell(2, 2)));
    }
}


