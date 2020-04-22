package Server;

import Messages.Message;
import Model.Card;
import Model.Cell;
import Model.Player;
import Util.Action;
import Util.Genre;

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
     * Gets the controller
     *
     * @return The controller
     */
    public Controller getController() {
        return controller;
    }

    /**
     * Adds a clientHandler
     *
     * @param clientHandler The ClientHaldler to be added
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
            if (nickname.equals(clientHandler.getNickname()) && clientHandler.isConnected()) {
                return clientHandler;
            }
        }
        return null;
    }

    /**
     * Manage the addition of the second or third player
     *
     * @param requestedNickname The requested username of the player
     * @param birthDate         The birth date of the player
     * @return true if the username is available and the request was successful, otherwise false
     */
    public boolean requestAddPlayer(String requestedNickname, Date birthDate) {
        synchronized (controller) {
            if (controller.checkNickname(requestedNickname)) {
                controller.addPlayer(requestedNickname, birthDate);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Manage the addition of the first player and the creation of the game
     *
     * @param requestedNickname The requested username of the player
     * @param birthDate         The birth date of the player
     * @param playersNumber     The number of the players
     */
    public void requestAddPlayer(String requestedNickname, Date birthDate, int playersNumber) {
        controller.addPlayer(requestedNickname, birthDate, playersNumber);
    }

    /**
     * Notify the controller that the main thread can go on
     */
    public void ready() {
        synchronized (controller) {
            controller.notifyAll();
        }
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
     * Send to the controller who will be the first player according to the challenger choice
     *
     * @param fistPlayerNickname The first player's nickname
     */
    public void setFirstPlayer(String fistPlayerNickname) {
        controller.setFirstPlayer(fistPlayerNickname);
    }

    /**
     * Sends a message to every client handler
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
     * Notify the controller that a player has disconnected and remove the related client handler from the list
     *
     * @param nickname The nickname of the disconnected user
     */
    public void setDisconnected(String nickname) {
        controller.hasDisconnected(nickname);
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
     * Sends a message to every client handler except one
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
     * Closes all the connections
     */
    public void closeAll() {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.isConnected()) {
                clientHandler.close();
            }
        }
    }
}
