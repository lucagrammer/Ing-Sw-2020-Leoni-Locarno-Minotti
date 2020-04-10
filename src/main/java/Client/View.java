package Client;

import Model.Card;

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

    void chooseCards(int numCards);

    void chooseCard(List<Card> possibleChoices);
}
