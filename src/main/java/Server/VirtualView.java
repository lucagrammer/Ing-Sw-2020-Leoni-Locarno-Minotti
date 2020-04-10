package Server;

import Model.Card;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The virtual view of the server
 */
public class VirtualView {
    private Controller controller;
    private List<ClientHandler> clientHandlers;

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

    public void ready() {
        synchronized (controller) {
            controller.notifyAll();
        }
    }

    public void hasChosenCards(List<Card> chosenCards) {
        controller.setCards(chosenCards);
    }

    public void setCard(Card choice, String nickname) {
        controller.setCard(choice, nickname);
    }
}
