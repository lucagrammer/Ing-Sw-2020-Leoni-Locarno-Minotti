package Model;

import Util.Color;
import Util.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    private int numPlayer;
    private List<Player> players;
    private Board board;
    private ArrayList<Card> usedCards;

    /**
     * Build the Game
     * @param player    the player who creates the game
     * @param numPlayer the number of players
     */
    public Game(Player player, int numPlayer){
        this.players = new ArrayList<>();
        this.players.add(player);
        this.numPlayer = numPlayer;
        this.usedCards = new ArrayList<>();
        this.board = new Board(this);
    }

    /**
     * Gets the board of the game
     * @return the board of the game
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Add the player to the game
     * @param player the new player
     */
    public void addPlayer(Player player){
        if(players.size() < numPlayer) {
            players.add(player);
            players = players.stream().sorted((o1, o2) -> o2.getDateOfBirth().compareTo(o1.getDateOfBirth())).collect(Collectors.toList());
        }
    }

    /**
     * Gets all the players of the game
     *
     * @return all the players of the game
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Gets a player by his color
     *
     * @param color the color of the player
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
     * Sets all the cards that will be used during the game
     *
     * @param cards the cards selected
     */
    public void setUsedCards(List<Card> cards) {
        usedCards.clear();
        usedCards.addAll(cards);
    }

    /**
     * Gets all the cards that will be used during the game
     * @return the cards
     */
    public List<Card> getUsedCards(){
        return new ArrayList<>(usedCards);
    }

    /**
     * Check if everything is ready to start the game
     * @return true if all the players has joined the game and the cards has been set
     */
    public boolean isReady(){
        return players.size() == numPlayer && usedCards.size() == numPlayer;
    }

    /**
     * Gets the youngest player who is older than a specified player
     * @param player the specified player
     * @return the youngest player who is older than the specified player
     */
    public Player getNextPlayer(Player player){
        if(player == null || !players.contains(player) || players.indexOf(player) == players.size() -1)
            return players.get(0);
        else
            return players.get(players.indexOf(player) +1);
    }

}