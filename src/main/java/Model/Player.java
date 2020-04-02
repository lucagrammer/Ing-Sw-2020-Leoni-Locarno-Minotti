package Model;

import Util.*;

import java.util.Date;

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
     * Get the male worker
     * @return the male worker
     */
    public Worker getMaleWorker() {
        return maleWorker;
    }

    /**
     * Get the female worker
     * @return the female worker
     */
    public Worker getFemaleWorker() {
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
     * @return the card
     */
    public Card getCard() {
        return card;
    }

    /**
     * Compares the nicknames of two players
     * @param player The second player
     * @return true if the players match
     */
    public boolean equals(Player player){
        return this.nickname.equals(player.nickname);
    }

}
