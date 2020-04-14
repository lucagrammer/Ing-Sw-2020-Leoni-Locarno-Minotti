package Server;

import Messages.ServerToClient.*;
import Model.Card;
import Model.Cell;
import Model.Game;
import Model.Player;
import Util.Color;
import Util.Genre;

import java.util.Date;
import java.util.List;

/**
 * Controller of the game
 */
public class Controller {
    private Game game;
    private VirtualView virtualView;
    private Player currentPlayer;

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

    /**
     * Manage the cards setup process
     *
     * @throws InterruptedException When the main thread is stopped
     */
    public void gameStarter() throws InterruptedException {
        synchronized (this) {
            while (!isReady()) {
                this.wait();
            }
        }
        System.out.println("> Status: Starting the game");

        // Challenger chooses cards
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
        virtualView.sendToEveryone(new CardInfo(possibleChoices));

        // Players chose cards
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
        currentPlayer.setCard(possibleChoices.get(0));
        System.out.println("> Status: " + currentPlayerNickname + " has " + currentPlayer.getCard().getName());

        // Notify all cards
        virtualView.sendToEveryone(new ShowCard(game.getPlayers()));

        // Challenger chooses the first player
        virtualView.getClientHandlerByNickname(challenger.getNickname()).send(new SelectFirst(game.getPlayersNickname()));
        synchronized (this) {
            while (null == this.currentPlayer) {
                this.wait();
            }
        }
        System.out.println("> Status: The first player will be " + this.currentPlayer.getNickname());

        firstTurn();
    }

    /**
     * Manage the first turn: color setup and positioning of the workers
     *
     * @throws InterruptedException When the main thread is stopped
     */
    private void firstTurn() throws InterruptedException {
        Player currentPlayer = this.currentPlayer;
        String currentPlayerNickname;
        List<String> availableColors = Color.allColorsToString();
        for (int i = 0; i < game.getPlayers().size(); i++) {
            currentPlayerNickname = currentPlayer.getNickname();
            virtualView.getClientHandlerByNickname(currentPlayerNickname).send(new PlayerInit(game, availableColors));
            synchronized (this) {
                while (currentPlayer.getOccupiedCells().size() < 2) {
                    this.wait();
                }
            }
            System.out.println("> It's " + currentPlayerNickname.toUpperCase() + "'s turn:" +
                    "\n\tColor: " + currentPlayer.getWorker(Genre.MALE).getColor() +
                    "\n\tMale Position: " + currentPlayer.getWorker(Genre.MALE).getPosition() +
                    "\n\tFemale Position: " + currentPlayer.getWorker(Genre.FEMALE).getPosition());
            availableColors.remove(currentPlayer.getWorker(Genre.MALE).getColor().toString());
            currentPlayer = game.getNextPlayer(currentPlayer);
        }
    }

    /**
     * Tests if the cards have been chosen by the challenger
     *
     * @return True if the cards have been chosen by the challenger
     */
    private boolean areCardsChosen() {
        return game.getUsedCards().size() == game.getPlayers().size();
    }

    /**
     * Setd the cards that have been chosen by the challenger
     *
     * @param chosenCards The list of chosen cards
     */
    public void setCards(List<Card> chosenCards) {
        game.setUsedCards(chosenCards);
        synchronized (this) {
            this.notifyAll();
        }
    }

    /**
     * Sets the card of a specified player
     *
     * @param chosenCard The chosen card
     * @param nickname   The player's nickname
     */
    public void setCard(Card chosenCard, String nickname) {
        game.getPlayerByNickname(nickname).setCard(chosenCard);
        synchronized (this) {
            this.notifyAll();
        }
    }

    /**
     * Sets who will be the first player
     *
     * @param fistPlayerNickname The nickname of the first player
     */
    public void setFirstPlayer(String fistPlayerNickname) {
        currentPlayer = game.getPlayerByNickname(fistPlayerNickname);
        synchronized (this) {
            this.notifyAll();
        }
    }

    /**
     * Sets the color of a specified player
     *
     * @param nickname The player's nickname
     * @param color    The chosen color
     */
    public void setColor(String nickname, String color) {
        Color myColor = Color.getColorByName(color);
        game.getPlayerByNickname(nickname).chooseColor(myColor);
    }

    /**
     * Sets the first position of a specified worker of a specified player
     *
     * @param nickname The player's nickname
     * @param genre    The genre of the worker
     * @param position The chosen position
     */
    public void setFirstPosition(String nickname, Genre genre, Cell position) {
        game.getPlayerByNickname(nickname).getWorker(genre).setPosition(position);
        synchronized (this) {
            notifyAll();
        }
    }
}
