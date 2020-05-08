package server;

import model.Card;
import model.Cell;
import model.Player;
import network.Message;
import network.messages.SetUpNewNickname;
import util.Action;
import util.Genre;
import util.exceptions.DisconnectionException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The virtual view of the server
 */
public class VirtualView {
    private final List<ClientHandler> clientHandlers;
    private final Controller controller;

    /**
     * Constructor: build the VirtualView
     *
     * @param controller The controller
     */
    public VirtualView(Controller controller) {
        this.clientHandlers = new ArrayList<>();
        this.controller = controller;
    }

    /**
     * Adds a clientHandler
     *
     * @param clientHandler The ClientHandler to be added
     */
    public void addClientHandler(ClientHandler clientHandler) {
        synchronized (clientHandlers) {
            clientHandlers.add(clientHandler);
        }
    }

    /**
     * Gets the server-side client handler by the nickname of the handled player
     *
     * @param nickname The nickname of the handled player
     * @return The server-side client handler of the player
     */
    public ClientHandler getClientHandlerByNickname(String nickname) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (nickname.equals(clientHandler.getNickname())) {
                return clientHandler;
            }
        }
        return null;
    }

    /**
     * Sends a message to every connected client handler
     *
     * @param message The message to be sent
     */
    public void sendToEveryone(Message message) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.isConnected())
                clientHandler.send(message);
        }
    }

    /**
     * Sends a message to every connected client handler except one
     *
     * @param message       The message to be sent
     * @param avoidedPlayer The exception
     */
    public void sendToEveryoneExcept(Message message, Player avoidedPlayer) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (!clientHandler.getNickname().equalsIgnoreCase(avoidedPlayer.getNickname()) && clientHandler.isConnected()) {
                clientHandler.send(message);
            }
        }
    }

    /**
     * Send to the controller the information of the player
     *
     * @param temporaryNickname The temporary nickname of the player
     * @param nickname          The requested nickname
     * @param birthDate         The birth date of the player
     * @param numPlayers        The number of the players of the game (ignored if the player isn't the first one)
     */
    public void setUpGame(String temporaryNickname, String nickname, Date birthDate, int numPlayers) {
        if (numPlayers == 2 || numPlayers == 3) {
            controller.setNumPlayers(numPlayers);
        }
        boolean isUniqueUsername = controller.setPlayerInfo(nickname, birthDate);
        if (!isUniqueUsername) {
            controller.setTemporaryPlayerInfo(temporaryNickname, birthDate);
            getClientHandlerByNickname(temporaryNickname).send(new SetUpNewNickname(temporaryNickname));
        } else {
            getClientHandlerByNickname(temporaryNickname).setNickname(nickname);
        }
        controller.wakeUpController();
    }

    /**
     * Send to the controller the new nickname of the player
     *
     * @param temporaryNickname The temporary nickname of the player
     * @param nickname          The requested nickname
     */
    public void setNewNickname(String temporaryNickname, String nickname) {
        boolean isUniqueUsername = controller.setNewNickname(temporaryNickname, nickname);
        if (!isUniqueUsername) {
            getClientHandlerByNickname(temporaryNickname).send(new SetUpNewNickname(temporaryNickname));
        } else {
            getClientHandlerByNickname(temporaryNickname).setNickname(nickname);
        }
        controller.wakeUpController();
    }

    /**
     * Send to the controller the cards chosen by the challenger
     *
     * @param chosenCards The chosen cards
     */
    public void setGameCards(List<Card> chosenCards) {
        controller.setCards(chosenCards);
    }

    /**
     * Send to the controller the card chosen by a specific player
     *
     * @param choice   The chosen card
     * @param nickname The nickname of the player
     */
    public void setPlayerCard(Card choice, String nickname) {
        controller.setCard(choice, nickname);
    }

    /**
     * Send to the controller the nickname of the player chosen to be the first player
     *
     * @param fistPlayerNickname The first player's nickname
     */
    public void setFirstPlayer(String fistPlayerNickname) {
        controller.setFirstPlayer(fistPlayerNickname);
    }

    /**
     * Send to the controller the chosen first position for the specified worker
     *
     * @param nickname The nickname of the player
     * @param genre    The genre of the worker
     * @param position The position of the worker
     */
    public void setPlayerPosition(String nickname, Genre genre, Cell position) {
        controller.setFirstPosition(nickname, genre, position);
    }

    /**
     * Send to the controller the chosen color of the player
     *
     * @param nickname The nickname of the player
     * @param color    The chosen color
     */
    public void setPlayerColor(String nickname, String color) {
        controller.setColor(nickname, color);
    }

    /**
     * Notify the controller that a player has chosen an action
     *
     * @param action The chosen action
     * @param player The nickname of the player
     */
    public void setAction(Action action, String player) {
        controller.setAction(action, player);
    }

    /**
     * Notify the controller that a player has disconnected
     *
     * @param nickname The nickname of the disconnected user
     */
    public void setDisconnected(String nickname) {
        controller.setAsDisconnected(nickname);
    }

    /**
     * Checks if the game is still active
     *
     * @return True if the game is still active, otherwise false
     */
    public boolean checkGameStatus() {
        boolean status;
        try {
            status = controller.isRunning();
        } catch (DisconnectionException e) {
            status = false;
        }
        return status;
    }

    /**
     * Closes all the connections to clients
     */
    public void closeAll() {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.isConnected()) {
                clientHandler.setDisconnected();
            }
        }
    }
}
