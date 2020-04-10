package Server;

import Messages.ServerToClient.ChooseCards;
import Messages.ServerToClient.SelectCard;
import Messages.ServerToClient.ShowCard;
import Model.Card;
import Model.Game;
import Model.Player;

import java.util.Date;
import java.util.List;

/**
 * Controller of the game
 */
public class Controller {
    private Game game;
    private VirtualView virtualView;

    /**
     * Creates the game and adds the first player
     *
     * @param nickname   The nickname of the first player
     * @param birthDate  The birth date of the first player
     * @param numPlayers The number of players of the game
     */
    public void addPlayer(String nickname, Date birthDate, int numPlayers) {
        Player player = new Player(nickname, birthDate);
        game = new Game(player, numPlayers);
    }

    /**
     * Adds a player to the existing game
     *
     * @param nickname  The nickname of the first player
     * @param birthDate The birth date of the first player
     */
    public void addPlayer(String nickname, Date birthDate) {
        Player player = new Player(nickname, birthDate);
        game.addPlayer(player);
    }

    /**
     * Sets the virtual view
     *
     * @param virtualView The virtual view
     */
    public void setVirtualView(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    /**
     * Tests if the game has been created and it's ready to start
     *
     * @return True if the game is ready to start.
     */
    public boolean isReady() {
        return (game != null) && game.isReady();
    }

    /**
     * Checks if a nickname is available
     *
     * @param requestedUsername The nickname to be checked
     * @return True if the username is available, otherwise false
     */
    public boolean checkNickname(String requestedUsername) {
        for (Player aPlayer : game.getPlayers()) {
            if (aPlayer.getNickname().equalsIgnoreCase(requestedUsername)) {
                return false;
            }
        }
        return true;
    }

    public void gameStarter() throws InterruptedException {
        synchronized (this) {
            while (!isReady()) {
                this.wait();
            }
        }
        System.out.println("> Status: Starting the game");

        List<Player> players = game.getPlayers();
        int randomNumber = (int) (Math.random() * (players.size()));
        Player challenger = players.get(randomNumber);
        virtualView.getClientHandlerByNickname(challenger.getNickname()).send(new ChooseCards(players.size()));
        System.out.println("> Status: Waiting for chosen cards");

        synchronized (this) {
            while (!(areCardsChosen())) {
                this.wait();
            }
        }
        System.out.println("> Status: Cards has been chosen");

        List<Card> possibleChoices = game.getUsedCards();
        Player currentPlayer = game.getNextPlayer(challenger);
        String currentPlayerNickname;
        for (int i = 0; i < game.getPlayers().size() - 1; i++) {
            currentPlayerNickname = currentPlayer.getNickname();
            virtualView.getClientHandlerByNickname(currentPlayerNickname).send(new SelectCard(possibleChoices));
            synchronized (this) {
                while (null == currentPlayer.getCard()) {
                    this.wait();
                }
            }
            System.out.println("> Status: " + currentPlayerNickname + " has chosen " + currentPlayer.getCard().getName());
            possibleChoices.remove(currentPlayer.getCard());
            currentPlayer = game.getNextPlayer(currentPlayer);
        }
        currentPlayerNickname = currentPlayer.getNickname();
        virtualView.getClientHandlerByNickname(currentPlayerNickname).send(new ShowCard(possibleChoices.get(0)));
        System.out.println("> Status: " + currentPlayerNickname + " has chosen " + currentPlayer.getCard().getName());

        // TODO continuare da qua
    }

    private boolean areCardsChosen() {
        return game.getUsedCards().size() == game.getPlayers().size();
    }

    public void setCards(List<Card> chosenCards) {
        game.setUsedCards(chosenCards);
        synchronized (this) {
            this.notifyAll();
        }
    }

    public void setCard(Card chosenCard, String nickname) {
        game.getPlayerByNickname(nickname).setCard(chosenCard);
        synchronized (this) {
            this.notifyAll();
        }
    }
}
