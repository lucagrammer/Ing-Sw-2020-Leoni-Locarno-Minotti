package model;

import org.junit.Test;
import util.Genre;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static util.PlayerColor.BLUE;

public class WorkerTest {

    private Worker worker;
    private Player player;

    @Test
    public void positionInfo_setPosition_getCorrectPosition() {
        worker = new Worker(Genre.FEMALE, player);
        Cell cell = new Cell(4, 3);
        worker.setPosition(cell);
        assertEquals(cell, worker.getPosition());
    }

    @Test
    public void setColor_getCorrectColor() {
        worker = new Worker(Genre.FEMALE, player);
        worker.setPlayerColor(BLUE);
        assertEquals(BLUE, worker.getPlayerColor());
    }

    @Test
    public void genreInfo_getCorrectGenre() {
        worker = new Worker(Genre.MALE, player);
        assertEquals(Genre.MALE, worker.getGenre());
    }

    @Test
    public void PlayerInfo_getCorrectPlayer() {
        String nickname = "John";
        player = new Player(nickname, new Date(4 / 5 / 1996), false);
        worker = new Worker(Genre.MALE, player);
        assertEquals(player, worker.getPlayer());
    }
}