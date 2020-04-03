package Model;

import Util.Color;
import Util.Genre;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Player {
    private String nickname;
    private Date dateOfBirth;
    private Worker maleWorker;
    private Worker femaleWorker;
    private Card card;

    /**
     * Constructor: build a player
     * @param nickname    Player's nickname
     * @param dateOfBirth Player's date of birth
     */
    public Player(String nickname, Date dateOfBirth){
        this.nickname = nickname;
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets the nickname of the player
     * @return Player's nickname
     */
    public String getNickname(){
        return nickname;
    }

    /**
     * Gets the date of birth of the player
     * @return Player's date of birth
     */
    public Date getDateOfBirth(){
        return dateOfBirth;
    }

    /**
     * The player chooses his color
     * @param color The color chosen by the player
     */
    public void chooseColor(Color color){
        this.maleWorker = new Worker(Genre.MALE, color, this);
        this.femaleWorker = new Worker(Genre.FEMALE, color, this);
    }

    /**
     * Get a specific worker of the player
     *
     * @param genre the genre of the worker
     * @return the worker
     */
    public Worker getWorker(Genre genre) {
        if (genre == Genre.MALE)
            return maleWorker;
        else
            return femaleWorker;
    }

    /**
     * The player chooses his card
     * @param card The card chosen by the player
     */
    public void setCard(Card card){
        this.card = card;
    }

    /**
     * Get the card
     *
     * @return the card
     */
    public Card getCard() {
        return card;
    }

    /**
     * Gets all the cells occupied by the workers of the player
     *
     * @return all the occupied cells
     */
    public List<Cell> getOccupiedCells() {
        List<Cell> occupiedCells = new ArrayList<>();

        occupiedCells.add(femaleWorker.getPosition());
        occupiedCells.add(maleWorker.getPosition());
        return occupiedCells;
    }

    /**
     * Compares the nicknames of two players
     *
     * @param player The second player
     * @return true if the players match
     */
    public boolean equals(Player player) {
        return this.nickname.equals(player.nickname);
    }

}
