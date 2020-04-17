package Client;

import Model.Card;
import Model.Game;
import Model.Player;
import Util.RoundActions;

import java.util.List;

/**
 * Manages the interface to the user
 */
public interface View {

    /**
     * Sets the serverHandler
     *
     * @param serverHandler The serverHandler
     */
    void setServerHandler(ServerHandler serverHandler);

    /**
     * Interface launcher. Asks the server IP to connect to and notify it to the serverHandler
     */
    void launch();

    /**
     * Shows a specified message to the user
     *
     * @param string The message to be shown
     */
    void showMessage(String string);

    /**
     * Asks a new nickname to the user and notify the choice to the serverHandler
     */
    void askNewNickname();

    /**
     * Asks nickname, birth date and if it's a new game the number of players
     * for the game and notify the information to the serverHandler
     *
     * @param newGame True if the it is a new game, otherwise false
     */
    void gameSetUp(boolean newGame);

    /**
     * Asks the game cards
     *
     * @param numCards Number of cards to be selected
     */
    void chooseCards(int numCards);

    /**
     * Asks the card the player whats to use during the game
     *
     * @param possibleChoices All the possible cards
     */
    void chooseCard(List<Card> possibleChoices);

    /**
     * Asks the nickname of the first player
     *
     * @param playersNicknames All the nicknames
     */
    void chooseFirstPlayer(List<String> playersNicknames);

    /**
     * Show all the cards of the game
     *
     * @param cards All the cards of the game
     */
    void showGameCards(List<Card> cards);

    /**
     * Shows th cards assignment of the game
     *
     * @param playerList The list of players of the game
     */
    void showCardAssignment(List<Player> playerList);

    /**
     * Asks the color the player whats to choose and the first position for the male and female worker
     *
     * @param availableColors All the available colors
     * @param game            The game
     */
    void chooseColorAndPosition(List<String> availableColors, Game game);

    /**
     * Shows the board of the game
     *
     * @param game The game
     */
    void showMap(Game game);

    void askAction(RoundActions roundActions);

    void showGameEnd(String winnerNickname, boolean youWin);

    void showDisconnection(String nickname);
}
