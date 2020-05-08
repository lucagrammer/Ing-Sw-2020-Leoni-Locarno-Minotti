package client;

import model.Card;
import model.Cell;
import network.CVMessage;
import network.MVMessage;
import network.Message;
import network.messages.*;
import network.ping.NetworkHandler;
import network.ping.PingSender;
import util.Action;
import util.Configurator;
import util.Genre;
import util.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;

/**
 * Manages communication from and to the server
 */
public class ServerHandler implements NetworkHandler {
    private final Object lock = new Object();
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket socket;
    private View view;
    private String nickname;
    private boolean isConnected = false;

    /**
     * Starts listening for server messages and execute them client-side
     */
    public void startListening() {
        while (isConnected) {
            try {
                Message serverMessage = (Message) input.readObject();
                if (serverMessage.getType() == MessageType.MV) {
                    if (serverMessage instanceof ShowDisconnection && isConnected) {
                        // Under control disconnection
                        isConnected = false;
                        closeConnection();
                    }
                    MVMessage mvMessage = (MVMessage) serverMessage;
                    mvMessage.execute(view);
                } else {
                    if (serverMessage.getType() == MessageType.CV) {
                        updateNickname(serverMessage);
                        CVMessage cvMessage = (CVMessage) serverMessage;
                        cvMessage.execute(view);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                if (isConnected) {
                    String errorMessage = "Server unreachable" + (Configurator.getErrorDetailsFlag() ? " during message reading" : "") + ".";
                    view.showErrorMessage(errorMessage, true);
                }
                isConnected = false;
            }
        }
    }

    /**
     * Update the nickname of the client if the message contains it
     *
     * @param serverMessage The message received
     */
    private void updateNickname(Message serverMessage) {
        if (serverMessage instanceof SetUpGame) {
            this.nickname = ((SetUpGame) serverMessage).getTemporaryNickname();
        }
        if (serverMessage instanceof SetUpNewNickname) {
            this.nickname = ((SetUpNewNickname) serverMessage).getTemporaryNickname();
        }
    }

    /**
     * Sets the view
     *
     * @param view The view
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Gets the nickname of the player
     *
     * @return The nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets connection to the specified server
     *
     * @param serverIP IP address of the server
     */
    public void setConnection(String serverIP) {
        try {
            socket = new Socket(serverIP, Configurator.getDefaultPort());
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            isConnected = true;

            // Sets the connection timeout to 20 seconds and start sending pings to the server every 5 seconds
            socket.setSoTimeout(20000);
            (new PingSender(this, false)).start();
        } catch (IOException e) {
            String errorMessage = "Server unreachable" + (Configurator.getErrorDetailsFlag() ? " during connection setup" : "");
            view.showErrorMessage(errorMessage, true);
        }

        startListening();
    }

    /**
     * Closes the connection to the server
     */
    public void closeConnection() {
        try {
            socket.close();
            isConnected = false;
        } catch (IOException e) {
            String errorMessage = "An error occurred " + (Configurator.getErrorDetailsFlag() ? "  when closing the connection" : "") + ".";
            view.showErrorMessage(errorMessage, true);
            e.printStackTrace();
        }
    }

    /**
     * Checks the connection status
     *
     * @return True if it is connected, otherwise false
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Sends a message
     *
     * @param message The message to be sent
     */
    public void send(Message message) {
        if (isConnected) {
            try {
                synchronized (lock) {
                    output.writeUnshared(message);
                    output.flush();
                    output.reset();
                }
            } catch (IOException e) {
                isConnected = false;
                String errorMessage = "Server unreachable" + (Configurator.getErrorDetailsFlag() ? " during message sending" : "") + ".";
                view.showErrorMessage(errorMessage, true);
            }
        }
    }

    /**
     * Prepares the request of SetUpGame with information about the player and the game
     *
     * @param nickname   The requested username
     * @param date       The date of birth
     * @param numPlayers The number of players of the game to be created (irrelevant if the player is joining an existing game)
     */
    public void sendSetUpGame(String nickname, Date date, int numPlayers) {
        send(new SetUpGame(nickname, date, numPlayers));
        this.nickname = nickname;
    }

    /**
     * Prepares the response to the server ResetUsernameProcess request
     *
     * @param newNickname The new requested nickname
     */
    public void sendNewNickname(String newNickname) {
        send(new SetUpNewNickname(nickname, newNickname));
        this.nickname = newNickname;
    }

    /**
     * Prepares the response to the server SetUpGameCards request
     *
     * @param chosenCards The chosen cards
     */
    public void sendGameCards(List<Card> chosenCards) {
        send(new SetUpGameCards(chosenCards));
    }

    /**
     * Prepares the response to the server SetUpPlayerCard request
     *
     * @param chosenCard The chosen card
     */
    public void sendPlayerCard(Card chosenCard) {
        send(new SetUpPlayerCard(chosenCard, nickname));
    }

    /**
     * Prepares the response to the server SetUpFirstPlayer request
     *
     * @param chosenNickname The first player's nickname
     */
    public void sendFirstPlayer(String chosenNickname) {
        send(new SetUpFirstPlayer(chosenNickname));
    }

    /**
     * Prepares the response to the server SetUpPlayerColor request
     *
     * @param chosenColor The chosen color
     */
    public void sendPlayerColor(String chosenColor) {
        send(new SetUpPlayerColor(chosenColor, nickname));
    }

    /**
     * Prepares the response to the server SetUpPlayerPosition request
     *
     * @param genre      The genre of the worker
     * @param chosenCell The position of the worker
     */
    public void sendPlayerPosition(Genre genre, Cell chosenCell) {
        send(new SetUpPlayerPosition(genre, chosenCell, nickname));
    }

    /**
     * Prepares the response to the server Turn request
     *
     * @param theAction The chosen action
     */
    public void sendAction(Action theAction) {
        send(new Turn(theAction, nickname));
    }

    /**
     * Prepare a new game or terminate the program
     *
     * @param choice The choice of the user
     */
    public void sendNewGame(boolean choice) {
        isConnected = false;
        closeConnection();
        if (choice) {
            ClientLauncher.main(null);
        }
    }
}