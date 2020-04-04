package Model;

import Util.Color;
import Util.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stores information about a game
 */
public class Game {
    private int numPlayer;
    private List<Player> players;
    private Board board;
    private ArrayList<Card> usedCards;

    /**
     * Constructor: build the Game
     *
     * @param player    The player who creates the game
     * @param numPlayer The number of players
     */
    public Game(Player player, int numPlayer) {
        this.players = new ArrayList<>();
        this.players.add(player);
        this.numPlayer = numPlayer;
        this.usedCards = new ArrayList<>();
        this.board = new Board();
    }

    /**
     * Gets the board of the game
     * @return The board of the game
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Adds the player to the game
     * @param player    The new player
     */
    public void addPlayer(Player player){
        if(players.size() < numPlayer) {
            players.add(player);
            players = players.stream().sorted((o1, o2) -> o2.getDateOfBirth().compareTo(o1.getDateOfBirth())).collect(Collectors.toList());
        }
    }

    /**
     * Gets all the players of the game
     * @return A list of the players of the game
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Gets a player by his color
     *
     * @param color The color of the player
     * @return the player associated to the color or null value if there isn't a player with that color
     */
    public Player getPlayersByColor(Color color) {
        for (Player player : players) {
            if (player.getWorker(Genre.MALE).getColor() == color)
                return player;
        }
        return null;
    }

    /**
     * Gets all the cards that will be used during the game
     *
     * @return A list containing all the used cards
     */
    public List<Card> getUsedCards() {
        return new ArrayList<>(usedCards);
    }

    /**
     * Sets all the cards that will be used during the game
     *
     * @param cards The cards selected
     */
    public void setUsedCards(List<Card> cards) {
        usedCards.clear();
        usedCards.addAll(cards);
    }

    /**
     * Check if everything is ready to start the game
     * @return True if all the players has joined the game and the cards has been set
     */
    public boolean isReady(){
        return players.size() == numPlayer && usedCards.size() == numPlayer;
    }

    /**
     * Gets the youngest player who is older than a specified player
     * @param player    The specified player
     * @return The youngest player who is older than the specified player
     */
    public Player getNextPlayer(Player player){
        if(player == null || !players.contains(player) || players.indexOf(player) == players.size() -1)
            return players.get(0);
        else
            return players.get(players.indexOf(player) +1);
    }

}