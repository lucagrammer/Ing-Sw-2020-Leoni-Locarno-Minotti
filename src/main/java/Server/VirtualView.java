package Server;

import Messages.Message;
import Model.Card;
import Model.Cell;
import Util.Genre;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The virtual view of the server
 */
public class VirtualView {
    private final List<ClientHandler> clientHandlers;
    private Controller controller;

    /**
     * Constructor: build the VirtualView
     */
    public VirtualView() {
        clientHandlers = new ArrayList<>();
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
     * Sets the controller
     *
     * @param controller The controller
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Adds a clientHandler
     *
     * @param clientHandler The ClientHaldler to be added
     */
    public void addClientHandler(ClientHandler clientHandler) {
        clientHandlers.add(clientHandler);
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
    public void setCard(Card choice, String nickname) {
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
    public void setPosition(String nickname, Genre genre, Cell position) {
        controller.setFirstPosition(nickname, genre, position);
    }

    /**
     * Send to the controller the chosen color of the player
     *
     * @param nickname The nickname of the player
     * @param color    The chosen color
     */
    public void setColor(String nickname, String color) {
        controller.setColor(nickname, color);
    }
}
