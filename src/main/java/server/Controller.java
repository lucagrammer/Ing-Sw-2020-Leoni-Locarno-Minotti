package server;

import model.Card;
import model.Cell;
import model.Game;
import model.Player;
import network.messages.*;
import util.*;
import util.exceptions.DisconnectionException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller of the game: manages the game flow
 */
public class Controller {
    private final Game game;
    private VirtualView virtualView;
    private Player currentPlayer;

    /**
     * Constructor: build a Controller
     *
     * @param game The game to be controlled
     */
    public Controller(Game game) {
        this.game = game;
    }

    /**
     * Sets the virtual view to communicate with
     *
     * @param virtualView The virtual view to communicate with
     */
    public void setVirtualView(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    /**
     * Manages the whole game cards setup process from their selection to their assignment
     *
     * @throws InterruptedException   When the thread is interrupted
     * @throws DisconnectionException When the a blocking disconnection event happens
     */
    public void gameStarter() throws InterruptedException, DisconnectionException {
        synchronized (this) {
            while (!gameCanStart() && isRunning()) {
                this.wait();
            }
        }
        System.out.println("> Status: Starting the game");

        // Challenger chooses cards
        List<Player> players = game.getPlayers();
        int randomNumber = (int) (Math.random() * (players.size()));
        Player challenger = players.get(randomNumber);
        virtualView.getClientHandlerByNickname(challenger.getNickname()).send(new SetUpGameCards(players.size()));
        System.out.println("> Status: Waiting for chosen cards");
        synchronized (this) {
            while (!(areCardsChosen()) && isRunning()) {
                this.wait();
            }
        }
        System.out.println("> Status: Cards has been chosen");

        List<Card> possibleChoices = game.getUsedCards();
        virtualView.sendToEveryone(new ShowGameCards(possibleChoices));

        currentPlayer = game.getNextPlayer(challenger);
        manageCardChoice(possibleChoices);
        // Show cards to everyone
        virtualView.sendToEveryone(new ShowCardAssignment(game.getPlayers()));

        manageFirstPlayerChoice(challenger);
        firstTurn();
    }

    /**
     * Manages the first turn: color setup and positioning of the workers
     *
     * @throws InterruptedException   When the thread is interrupted
     * @throws DisconnectionException When the a blocking disconnection event happens
     */
    private void firstTurn() throws InterruptedException, DisconnectionException {
        String currentNickname;
        ClientHandler currentClientHandler;
        ArrayList<String> availableColors = PlayerColor.allColorsToString();

        for (int i = 0; i < game.getPlayers().size(); i++) {
            currentNickname = currentPlayer.getNickname();
            currentClientHandler = virtualView.getClientHandlerByNickname(currentNickname);
            System.out.println("> Status: waiting for color choice of " + currentPlayer.getNickname());

            virtualView.sendToEveryoneExcept(new ShowMap(new MapInfo(game), currentNickname, null), currentPlayer);

            // PlayerColor setup
            currentClientHandler.send(new SetUpPlayerColor(availableColors));
            synchronized (this) {
                while (currentPlayer.getColor() == null && isRunning()) {
                    this.wait();
                }
            }
            availableColors.remove(currentPlayer.getColor().toString());
            System.out.println("> Player info: " + currentNickname.toUpperCase() +
                    "\n\tcolor: " + currentPlayer.getColor());

            // Position setup
            for (Genre genre : Genre.values()) {
                // Calculate forbidden cells
                List<Cell> forbiddenCells = new ArrayList<>();
                for (Player player : game.getPlayers()) {
                    forbiddenCells.addAll(player.getOccupiedCells());
                }

                currentClientHandler.send(new SetUpPlayerPosition(genre, forbiddenCells, new MapInfo(game)));
                synchronized (this) {
                    while (currentPlayer.getWorker(genre).getPosition() == null && isRunning()) {
                        this.wait();
                    }
                }
                System.out.println("\t" + genre.name().toLowerCase() + " position: " + currentPlayer.getWorker(genre).getPosition());
            }

            currentPlayer = game.getNextPlayer(currentPlayer);
        }
        game();
    }

    /**
     * Manages the turns and calculate all the possible actions
     *
     * @throws InterruptedException   When the thread is interrupted
     * @throws DisconnectionException When the a blocking disconnection event happens
     */
    private void game() throws InterruptedException, DisconnectionException {
        String loserNickname = null;

        while (!game.hasWinner()) {
            RoundActions possibleActions = calculatePossibleActions();
            // Contains the lose action?
            if (possibleActions.hasLost()) {
                loserNickname = currentPlayer.getNickname();
                System.out.println("> Status: " + loserNickname + " has lost");
                manageLose();
            } else {
                // The player has ended his turn or can now only end his turn?
                if (currentPlayer.getRoundActions().hasEnded() || possibleActions.mustEnd()) {
                    currentPlayer = game.getNextPlayer(currentPlayer);
                    // Flush the actions of the next player
                    currentPlayer.setRoundActions(new RoundActions());
                } else {
                    // Update the map for everyone
                    virtualView.sendToEveryoneExcept(new ShowMap(new MapInfo(game), currentPlayer.getNickname(), loserNickname), currentPlayer);

                    int currentActionsNumber = currentPlayer.getRoundActions().getActionList().size();
                    virtualView.getClientHandlerByNickname(currentPlayer.getNickname()).send(new Turn(possibleActions, new MapInfo(game), loserNickname));
                    loserNickname = null;
                    synchronized (this) {
                        while (currentPlayer.getRoundActions().getActionList().size() <= currentActionsNumber && isRunning()) {
                            this.wait();
                        }
                    }
                }
            }
        }
        manageWin();
    }

    /**
     * Calculates all the possible actions of the current player
     *
     * @return The round actions of the current player
     */
    private RoundActions calculatePossibleActions() {
        RoundActions roundActions = currentPlayer.getCard().getRules().nextPossibleActions(currentPlayer, game);
        for (Player player : game.getPlayers()) {
            roundActions = player.getCard().getEnemyRules().fixEnemyActions(roundActions, game, player);
        }
        return roundActions;
    }

    /**
     * Manages the choice of the card by each player
     *
     * @throws InterruptedException   When the thread is interrupted
     * @throws DisconnectionException When the a blocking disconnection event happens
     */
    private void manageCardChoice(List<Card> possibleChoices) throws DisconnectionException, InterruptedException {
        String currentNickname;
        for (int i = 0; i < game.getPlayers().size() - 1; i++) {
            currentNickname = currentPlayer.getNickname();
            virtualView.getClientHandlerByNickname(currentNickname).send(new SetUpPlayerCard(possibleChoices));
            synchronized (this) {
                while (null == currentPlayer.getCard() && isRunning()) {
                    this.wait();
                }
            }

            System.out.println("> Status: " + currentNickname + " has chosen " + currentPlayer.getCard().getName() + " card");
            possibleChoices.remove(currentPlayer.getCard());
            currentPlayer = game.getNextPlayer(currentPlayer);
        }
        currentNickname = currentPlayer.getNickname();
        currentPlayer.setCard(possibleChoices.get(0));
        System.out.println("> Status: " + currentNickname + " has " + currentPlayer.getCard().getName() + " card");
    }

    /**
     * Manage the first player choice by the challenger
     *
     * @param challenger The challenger
     * @throws InterruptedException   When the thread is interrupted
     * @throws DisconnectionException When the a blocking disconnection event happens
     */
    private void manageFirstPlayerChoice(Player challenger) throws InterruptedException, DisconnectionException {
        currentPlayer = null;
        virtualView.getClientHandlerByNickname(challenger.getNickname()).send(new SetUpFirstPlayer(game.getPlayersNickname()));
        synchronized (this) {
            while (null == this.currentPlayer && isRunning()) {
                this.wait();
            }
        }
        System.out.println("> Status: The first player will be " + currentPlayer.getNickname());
    }

    /**
     * Manages the win of the current player: update the final map and then notify everyone
     */
    private void manageWin() {
        virtualView.sendToEveryone(new ShowMap(new MapInfo(game), currentPlayer.getNickname(), null));
        System.out.println("> Game ended: " + currentPlayer.getNickname() + " has won");
        for (Player p : game.getAllPlayers()) {
            virtualView.getClientHandlerByNickname(p.getNickname()).send(new ShowGameEndMessage(currentPlayer.getNickname(), currentPlayer.equals(p)));
        }
    }

    /**
     * Manages the lose of the current player: end the game or remove the player
     */
    public void manageLose() {
        removePlayer(currentPlayer);

        currentPlayer = game.getNextPlayer(currentPlayer);

        // The game must ended?
        if (game.getPlayers().size() == 1) {
            System.out.println("> Status: " + game.getPlayers().get(0).getNickname() + " has won");
            game.getPlayers().get(0).setWinner(true);
        } else {
            // Flush the actions of the next player
            currentPlayer.setRoundActions(new RoundActions());
        }
    }

    /**
     * Tests if the game is ready to start and if everyone has a final nickname
     *
     * @return True if the game is ready to start
     */
    public boolean gameCanStart() {
        return (game != null) && game.canStart();
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
     * Creates the game and adds the first player
     *
     * @param numPlayers The number of players of the game
     */
    public void setNumPlayers(int numPlayers) {
        game.setNumPlayers(numPlayers);
        wakeUpServerLauncher();
    }

    /**
     * Sets player info if the username is unique
     *
     * @param nickname  The requested nickname
     * @param birthDate The birth date of the player
     * @return True if the player has been added, otherwise false
     */
    public boolean setPlayerInfo(String nickname, Date birthDate) {
        synchronized (this) {
            if (checkNickname(nickname)) {
                Player player = new Player(nickname, birthDate, false);
                game.addPlayer(player);
                return true;
            }
        }
        System.out.println(Frmt.color('y', "> Warning: someone is trying to connect with a nickname already in use"));
        return false;
    }

    /**
     * Sets temporary player info
     *
     * @param temporaryUsername The temporary nickname
     * @param birthDate         The birth date of the player
     */
    public void setTemporaryPlayerInfo(String temporaryUsername, Date birthDate) {
        synchronized (this) {
            Player player = new Player(temporaryUsername, birthDate, true);
            game.addPlayer(player);
        }
    }

    /**
     * Update the player info with a final nickname if it's unique
     *
     * @param nickname The requested nickname
     * @return True if the player info has been updated, otherwise false
     */
    public boolean setNewNickname(String temporaryNickname, String nickname) {
        synchronized (this) {
            if (checkNickname(nickname)) {
                game.getPlayerByNickname(temporaryNickname).setFinalNickname(nickname);
                return true;
            }
        }
        System.out.println(Frmt.color('y', "> Warning: someone is trying to connect with a nickname already in use"));
        return false;
    }

    /**
     * Sets the cards that have been chosen by the challenger
     *
     * @param chosenCards The list of chosen cards
     */
    public void setCards(List<Card> chosenCards) {
        game.setUsedCards(chosenCards);
        wakeUpController();
    }

    /**
     * Sets the card of a specified player
     *
     * @param chosenCard The chosen card
     * @param nickname   The player's nickname
     */
    public void setCard(Card chosenCard, String nickname) {
        game.getPlayerByNickname(nickname).setCard(chosenCard);
        wakeUpController();
    }

    /**
     * Sets who will be the first player
     *
     * @param fistPlayerNickname The nickname of the first player
     */
    public void setFirstPlayer(String fistPlayerNickname) {
        currentPlayer = game.getPlayerByNickname(fistPlayerNickname);
        wakeUpController();
    }

    /**
     * Sets the color of a specified player
     *
     * @param nickname The player's nickname
     * @param color    The chosen color
     */
    public void setColor(String nickname, String color) {
        PlayerColor myPlayerColor = PlayerColor.getColorByName(color);
        game.getPlayerByNickname(nickname).chooseColor(myPlayerColor);
        wakeUpController();
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
        wakeUpController();
    }

    /**
     * Performs an action of a player
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
        wakeUpController();
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
     * Notify the other users and end the game if the player is not a loser
     *
     * @param nickname The nickname of the disconnected user
     */
    public void setAsDisconnected(String nickname) {
        Player disconnectedPlayer = game.getPlayerByNickname(nickname);
        if (disconnectedPlayer != null && disconnectedPlayer.isLoser()) {
            disconnectedPlayer.setConnected(false);
            return;
        }

        virtualView.sendToEveryone(new ShowDisconnection(nickname));
        virtualView.closeAll();
        game.setInactive();
        wakeUpController();
        wakeUpServerLauncher();
    }

    /**
     * Checks if the game is still active
     *
     * @return True if the game is still active
     * @throws DisconnectionException When the game is not active
     */
    public boolean isRunning() throws DisconnectionException {
        if (!game.isActive()) {
            throw new DisconnectionException();
        }
        return true;
    }

    /**
     * Notifies the Controller that it can proceed with execution
     */
    public void wakeUpController() {
        synchronized (this) {
            this.notifyAll();
        }
    }

    /**
     * Notifies the ServerLauncher that it can proceed with execution
     */
    public void wakeUpServerLauncher() {
        synchronized (game) {
            game.notifyAll();
        }
    }
}