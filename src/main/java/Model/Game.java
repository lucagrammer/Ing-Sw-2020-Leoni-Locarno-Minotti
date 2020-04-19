package Model;

import Util.Color;
import Util.Genre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game implements Serializable {
    private final int numPlayer;
    private final Board board;
    private final List<Card> usedCards;
    private List<Player> players;

    /**
     * Build the Game
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
     *
     * @return The board of the game
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Add the player to the game
     *
     * @param player The new player
     */
    public void addPlayer(Player player) {
        if (players.size() < numPlayer) {
            players.add(player);
            players = players.stream().sorted((o1, o2) -> o2.getDateOfBirth().compareTo(o1.getDateOfBirth())).collect(Collectors.toList());
        }
    }

    /**
     * Gets all the connected and non-loser players of the game
     *
     * @return All the connected and non-loser players of the game
     */
    public List<Player> getPlayers() {
        List<Player> returnedPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.isConnected() && !player.isLoser()) {
                returnedPlayers.add(player);
            }
        }
        return returnedPlayers;
    }

    /**
     * Gets all the nickname of the players
     *
     * @return All the nicknames
     */
    public List<String> getPlayersNickname() {
        List<String> nicknames = new ArrayList<>();
        for (Player player : players) {
            nicknames.add(player.getNickname());
        }
        return nicknames;
    }

    /**
     * Gets a player by his color
     *
     * @param color The color of the player
     * @return The player associated to the color or null value if there isn't a player with that color
     */
    public Player getPlayerByColor(Color color) {
        for (Player player : players) {
            if (player.getWorker(Genre.MALE).getColor() == color)
                return player;
        }
        return null;
    }

    /**
     * Gets a player by his nickname
     *
     * @param nickname The nickname of the player
     * @return The player associated to the color or null value if there isn't a player with that color
     */
    public Player getPlayerByNickname(String nickname) {
        for (Player player : players) {
            if (player.getNickname().equalsIgnoreCase(nickname))
                return player;
        }
        return null;
    }

    /**
     * Gets all the cards that will be used during the game
     *
     * @return The cards
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
     *
     * @return True if all the players has joined the game
     */
    public boolean isReady() {
        return players.size() == numPlayer;
    }

    /**
     * Gets the youngest connected and non-loser player who is older than a specified player
     *
     * @param player The specified player
     * @return The youngest player who is older than the specified player
     */
    public Player getNextPlayer(Player player) {
        Player chosenPlayer;
        if (player == null || !players.contains(player) || players.indexOf(player) == players.size() - 1)
            chosenPlayer = players.get(0);
        else
            chosenPlayer = players.get(players.indexOf(player) + 1);
        if (chosenPlayer.isConnected() && !chosenPlayer.isLoser()) {
            return chosenPlayer;
        } else {
            return getNextPlayer(chosenPlayer);
        }
    }

    /**
     * Tests if the game has a winner
     *
     * @return True if the game has a winner, otherwise false
     */
    public boolean hasWinner() {
        for (Player player : players) {
            if (player.isWinner()) {
                return true;
            }
        }
        return false;
    }
}