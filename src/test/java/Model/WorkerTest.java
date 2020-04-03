package Model;

import Util.Color;
import Util.Genre;
import org.junit.Test;

import java.util.Date;

import static Util.Color.BLUE;
import static org.junit.Assert.*;

public class WorkerTest {

    Worker worker;
    Cell cell;
    Player player;
    Board board;

    @Test
    public void positionInfo_setPosition_getCorrectPosition() {
        worker = new Worker(Genre.FEMALE, BLUE, player);
        cell= new Cell(4,3, board);
        worker.setPosition(cell);
        assertEquals(cell, worker.getPosition());
    }

    @Test
    public void ColorInfo_getCorrectColor() {
        worker = new Worker(Genre.FEMALE, BLUE, player);
        Color color= BLUE;
        assertEquals(color, worker.getColor());
    }

    @Test
    public void genreInfo_getCorrectGenre() {
        worker = new Worker(Genre.MALE, BLUE, player);
        assertEquals(Genre.MALE, worker.getGenre());
    }

    @Test
    public void PlayerInfo_getCorrectPlayer() {
        String nickname= "John";
        player = new Player (nickname, new Date(4/5/1996));
        worker = new Worker(Genre.MALE, BLUE, player);
        assertEquals(player, worker.getPlayer());
    }
}