package Model;

import Util.Action;
import Util.Color;
import Util.Genre;
import Util.RoundActions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Stores information about a player
 */
public class Player {
    private String nickname;
    private Date dateOfBirth;
    private Worker maleWorker;
    private Worker femaleWorker;
    private Card card;
    private RoundActions roundActions;

    /**
     * Constructor: build a player
     * @param nickname    Player's nickname
     * @param dateOfBirth Player's date of birth
     */
    public Player(String nickname, Date dateOfBirth){
        this.nickname = nickname;
        this.dateOfBirth = dateOfBirth;
        roundActions = new RoundActions();
    }

    /**
     * Gets the nickname of the player
     *
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
     * @param genre The genre of the worker
     * @return The worker
     */
    public Worker getWorker(Genre genre) {
        if (genre == Genre.MALE)
            return maleWorker;
        else
            return femaleWorker;
    }

    /**
     * Get a worker of the player by the occupied position
     * @param cell  The cell
     * @return The worker that occupies the specified cell or null value
     */
    public Worker getWorkerByPosition(Cell cell) {
        if (maleWorker.getPosition().equals(cell))
            return maleWorker;
        else {
            if (femaleWorker.getPosition().equals(cell))
                return femaleWorker;
            else
                return null;
        }
    }

    /**
     * Get the card of the player
     *
     * @return The card of the player
     */
    public Card getCard() {
        return card;
    }

    /**
     * Set the card of the player
     *
     * @param card The card chosen by the player
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * Gets all the cells occupied by the workers of the player
     *
     * @return All the occupied cells
     */
    public List<Cell> getOccupiedCells() {
        List<Cell> occupiedCells = new ArrayList<>();

        occupiedCells.add(femaleWorker.getPosition());
        occupiedCells.add(maleWorker.getPosition());
        return occupiedCells;
    }

    /**
     * Compares the nicknames of two players
     * @param player    The second player
     * @return true if the players match
     */
    public boolean equals(Player player) {
        return this.nickname.equals(player.nickname);
    }

    /**
     * Gets all the actions of the player during the current round
     * @return The actions of the player during the current round
     */
    public RoundActions getRoundActions() {
        return roundActions;
    }

    /**
     * Sets the actions of the player during the current round
     * @param roundActions  The actions of the player during the current round
     */
    public void setRoundActions(RoundActions roundActions) {
        this.roundActions = roundActions;
    }

    /**
     * Adds a new action of the player for the current round
     * @param action    The new action of the player during the current round
     */
    public void registerAction(Action action) {
        roundActions.add(action);
    }

}
