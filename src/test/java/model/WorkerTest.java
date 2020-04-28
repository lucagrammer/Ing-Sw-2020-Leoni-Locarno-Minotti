package model;

import Util.Color;
import Util.Genre;
import org.junit.Test;

import java.util.Date;

import static Util.Color.BLUE;
import static org.junit.Assert.assertEquals;

public class WorkerTest {

    Worker worker;
    Cell cell;
    Player player;
    Color color;

    @Test
    public void positionInfo_setPosition_getCorrectPosition() {
        worker = new Worker(Genre.FEMALE, player);
        cell = new Cell(4, 3);
        worker.setPosition(cell);
        assertEquals(cell, worker.getPosition());
    }

    @Test
    public void setColor_getCorrectColor() {
        worker = new Worker(Genre.FEMALE, player);
        color= BLUE;
        worker.setColor(color);
        assertEquals(BLUE, worker.getColor());
    }

    @Test
    public void genreInfo_getCorrectGenre() {
        worker = new Worker(Genre.MALE, player);
        assertEquals(Genre.MALE, worker.getGenre());
    }

    @Test
    public void PlayerInfo_getCorrectPlayer() {
        String nickname= "John";
        player = new Player (nickname, new Date(4/5/1996));
        worker = new Worker(Genre.MALE, player);
        assertEquals(player, worker.getPlayer());
    }
}