package Server;

import Messages.ServerToClient.*;
import Model.Card;
import Model.Cell;
import Model.Game;
import Model.Player;
import Util.*;
import Util.exceptions.MustRestartException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller of the game
 */
public class Controller {
    private Game game;
    private VirtualView virtualView;
    private Player currentPlayer;
    private boolean mustReset = false;

    /**
     * Manages the cards setup process
     *
     * @throws InterruptedException When the main thread is stopped
     * @throws MustRestartException When the game must restart
     */
    public void gameStarter() throws InterruptedException, MustRestartException {
        synchronized (this) {
            while (!isReady() && !mustReset) {
                this.wait();
            }
        }
        if (mustReset) {
            reset();
        }
        System.out.println("> Status: Starting the game");

        // Challenger chooses cards
        List<Player> players = game.getPlayers();
        int randomNumber = (int) (Math.random() * (players.size()));
        Player challenger = players.get(randomNumber);
        virtualView.getClientHandlerByNickname(challenger.getNickname()).send(new SetUpGameCards(players.size()));
        System.out.println("> Status: Waiting for chosen cards");

        synchronized (this) {
            while (!(areCardsChosen()) && !mustReset) {
                this.wait();
            }
        }
        if (mustReset) {
            reset();
        }

        System.out.println("> Status: Cards has been chosen");
        List<Card> possibleChoices = game.getUsedCards();
        virtualView.sendToEveryone(new ShowGameCards(possibleChoices));

        // Players chose cards
        Player currentPlayer = game.getNextPlayer(challenger);
        String currentPlayerNickname;
        for (int i = 0; i < game.getPlayers().size() - 1; i++) {
            currentPlayerNickname = currentPlayer.getNickname();
            virtualView.getClientHandlerByNickname(currentPlayerNickname).send(new SetUpPlayerCard(possibleChoices));
            synchronized (this) {
                while (null == currentPlayer.getCard() && !mustReset) {
                    this.wait();
                }
            }
            if (mustReset) {
                reset();
            }

            System.out.println("> Status: " + currentPlayerNickname + " has chosen " + currentPlayer.getCard().getName() + " card");
            possibleChoices.remove(currentPlayer.getCard());
            currentPlayer = game.getNextPlayer(currentPlayer);
        }
        currentPlayerNickname = currentPlayer.getNickname();
        currentPlayer.setCard(possibleChoices.get(0));
        System.out.println("> Status: " + currentPlayerNickname + " has " + currentPlayer.getCard().getName() + " card");

        // Show cards to everyone
        virtualView.sendToEveryone(new ShowCardAssignment(game.getPlayers()));
        if (mustReset) {
            reset();
        }

        // Challenger chooses the first player
        virtualView.getClientHandlerByNickname(challenger.getNickname()).send(new SetUpFirstPlayer(game.getPlayersNickname()));
        synchronized (this) {
            while (null == this.currentPlayer && !mustReset) {
                this.wait();
            }
            if (mustReset) {
                reset();
            }
        }
        System.out.println("> Status: The first player will be " + this.currentPlayer.getNickname());

        firstTurn();
    }

    /**
     * Manages the first turn: color setup and positioning of the workers
     *
     * @throws InterruptedException When the main thread is stopped
     * @throws MustRestartException When the game must restart
     */
    private void firstTurn() throws InterruptedException, MustRestartException {
        String currentPlayerNickname;
        ClientHandler currentPlayerClientHandler;
        List<String> availableColors = Color.allColorsToString();

        for (int i = 0; i < game.getPlayers().size(); i++) {
            currentPlayerNickname = currentPlayer.getNickname();
            currentPlayerClientHandler = virtualView.getClientHandlerByNickname(currentPlayerNickname);

            // Color setup
            currentPlayerClientHandler.send(new SetUpPlayerColor(availableColors));
            synchronized (this) {
                while (currentPlayer.getColor() == null && !mustReset) {
                    this.wait();
                }
            }
            if (mustReset) {
                reset();
            }
            availableColors.remove(currentPlayer.getColor().toString());
            System.out.println("> Player info: " + currentPlayerNickname.toUpperCase() +
                    "\n\tcolor: " + currentPlayer.getColor());

            // Position setup
            for (Genre genre : Genre.values()) {
                // Calculate forbidden cells
                List<Cell> forbiddenCells = new ArrayList<>();
                for (Player player : game.getPlayers()) {
                    forbiddenCells.addAll(player.getOccupiedCells());
                }

                currentPlayerClientHandler.send(new SetUpPlayerPosition(genre, forbiddenCells, game));
                synchronized (this) {
                    while (currentPlayer.getWorker(genre).getPosition() == null && !mustReset) {
                        this.wait();
                    }
                }
                if (mustReset) {
                    reset();
                }
                System.out.println("\t" + genre.name().toLowerCase() + " position: " + currentPlayer.getWorker(genre).getPosition());
            }

            currentPlayer = game.getNextPlayer(currentPlayer);
        }
        game();
    }

    /**
     * Manages the turns and the actions
     *
     * @throws InterruptedException When the main thread is stopped
     * @throws MustRestartException When the game must restart
     */
    private void game() throws InterruptedException, MustRestartException {
        String loserNickname = null;

        while (!game.hasWinner() && !mustReset) {
            // Find the possible actions
            RoundActions roundActions = currentPlayer.getCard().getRules().nextPossibleActions(currentPlayer, game);
            for (Player player : game.getPlayers()) {
                roundActions = player.getCard().getEnemyRules().fixEnemyActions(roundActions, game, player);
            }

            List<Action> roundActionsList = roundActions.getActionList();
            // Contains the lose action?
            if (roundActionsList.get(0).getActionType().equals(ActionType.LOSE)) {
                loserNickname = currentPlayer.getNickname();
                removePlayer(currentPlayer);

                // The game must ended?
                if (game.getPlayers().size() == 1) {
                    game.getPlayers().get(0).setWinner(true);

                } else {
                    currentPlayer = game.getNextPlayer(currentPlayer);
                    // Flush the actions of the next player
                    currentPlayer.setRoundActions(new RoundActions());
                }
            } else {
                // The player has ended his turn or can now only end his turn?
                if (currentPlayer.getRoundActions().hasEnded() || (roundActionsList.size() == 1 &&
                        roundActionsList.get(0).getActionType().equals(ActionType.END))) {
                    currentPlayer = game.getNextPlayer(currentPlayer);
                    // Flush the actions of the next player
                    currentPlayer.setRoundActions(new RoundActions());
                } else {
                    // Update the map for everyone
                    virtualView.sendToEveryoneExcept(new ShowMap(game, currentPlayer.getNickname(), loserNickname), currentPlayer);

                    int currentActionsNumber = currentPlayer.getRoundActions().getActionList().size();
                    virtualView.getClientHandlerByNickname(currentPlayer.getNickname()).send(new Turn(roundActions, game, loserNickname));
                    loserNickname = null;
                    synchronized (this) {
                        while (currentPlayer.getRoundActions().getActionList().size() <= currentActionsNumber && !mustReset) {
                            this.wait();
                        }
                        if (mustReset) {
                            reset();
                        }
                    }
                }
            }
        }
        if (mustReset) {
            reset();
        }

        // Update the final map for everyone
        virtualView.sendToEveryoneExcept(new ShowMap(game, currentPlayer.getNickname(), null), currentPlayer);

        // There's a winner: notify everyone
        for (Player p : game.getPlayers()) {
            virtualView.getClientHandlerByNickname(p.getNickname()).send(new ShowGameEndMessage(currentPlayer.getNickname(), currentPlayer.equals(p)));
        }
        reset();
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
     * Sets a player as loser
     *
     * @param removedPlayer The player that has lost
     */
    private void removePlayer(Player removedPlayer) {
        removedPlayer.setLoser(true);
    }

    /**
     * Tests if the cards have been chosen by the challenger
     *
     * @return True if the cards have been chosen by the challenger
     */
    private boolean areCardsChosen() {
        return game.getUsedCards().size() >= game.getPlayers().size();
    }

    /**
     * Sets the cards that have been chosen by the challenger
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
        synchronized (this) {
            notifyAll();
        }
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

    /**
     * Notify the other users and end the game if the player is not a loser
     *
     * @param nickname The nickname of the disconnected user
     */
    public void hasDisconnected(String nickname) {
        // had the player lost? if so do nothing
        if (game != null) {
            Player disconnectedPlayer = game.getPlayerByNickname(nickname);
            if (disconnectedPlayer != null && disconnectedPlayer.isLoser() /*&& !game.hasWinner()*/) {
                return;
            }
        }
        // already reset?
        if (!mustReset) {
            virtualView.sendToEveryone(new ShowDisconnection(nickname));

            mustReset = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }

    /**
     * Permorms an action of a player
     *
     * @param action   The action to be performed
     * @param nickname The player that performs the action
     */
    public void setAction(Action action, String nickname) {
        Player thePlayer = game.getPlayerByNickname(nickname);
        boolean isWinner = thePlayer.getCard().getRules().doAction(action, thePlayer, game);
        if (isWinner) {
            thePlayer.setWinner(true);
        }
        synchronized (this) {
            notifyAll();
        }
    }

    /**
     * Prepares the server for a new game by completing the termination of the current game
     *
     * @throws MustRestartException When the server must restart
     */
    private void reset() throws MustRestartException {
        virtualView.closeAll();
        System.out.println(Frmt.color('r', "> Status: Game controller has been stopped"));
        throw new MustRestartException();
    }
}