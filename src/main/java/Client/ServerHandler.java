package Client;

import Messages.CVMessage;
import Messages.ClientToServer.SetUpGame;
import Messages.MVMessage;
import Messages.Message;
import Messages.PingMessage;
import Messages.ServerToClient.*;
import Model.Card;
import Model.Cell;
import Util.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;

/**
 * Manages communication from and to the server
 */
class ServerHandler {
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket socket;
    private View view;
    private String nickname;
    private boolean isConnected = false;
    private final Object lock = new Object();

    /**
     * Starts listening for server messages and execute them client-side
     */
    public void startListening() {
        while (isConnected) {
            try {
                Message serverMessage = (Message) input.readObject();
                if (serverMessage.getType() == MessageType.MV) {
                    MVMessage mvMessage = (MVMessage) serverMessage;
                    if (serverMessage instanceof ShowDisconnection) {
                        // Under control disconnection:
                        // The server will soon close the connection due to the disconnection of another player
                        isConnected = false;
                        //closeConnection(); TODO DISCONNESIONE TOLTA
                    }
                    mvMessage.execute(view);
                } else {
                    if (serverMessage.getType() == MessageType.CV) {
                        if (serverMessage instanceof PingMessage) {
                            System.out.println("Ricevo ping"); //TODO
                        }
                        CVMessage cvMessage = (CVMessage) serverMessage;
                        cvMessage.execute(view);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                view.showMessage(Frmt.color('r', "> Error: Could not contact the server."), true);
                //e.printStackTrace();
                isConnected = false;
            }
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

            socket.setSoTimeout(20000);
            (new Ping()).start();
        } catch (Exception e) {
            isConnected = false;
            view.showMessage(Frmt.color('r', "> Error: Server unreachable."), true);
            //e.printStackTrace();
        }
        if (isConnected) {
            startListening();
        }
    }

    /**
     * Closes the connection to the server
     */
    public void closeConnection() {
        try {
            socket.close();
            isConnected = false;
        } catch (IOException e) {
            System.out.println(Frmt.color('r', "Error: An error occurred when closing the connection"));
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to connected the server
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
                System.out.println(Frmt.color('r', "> Error: Could not contact the server"));
                e.printStackTrace();
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
        this.nickname = nickname;
        send(new SetUpGame(nickname, date, numPlayers));
    }

    /**
     * Prepares the response to the server ResetUsernameProcess request
     *
     * @param newNickname The new requested nickname
     */
    public void sendNewNickname(String newNickname) {
        this.nickname = newNickname;
        send(new SetUpNewNickname(newNickname));
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
        if (choice) {
            ClientLauncher.main(null);
        }
    }


    class Ping extends Thread {

        public void run() {
            while (isConnected) {
                send(new PingMessage(false));
                System.out.println("\n\n>SENDING PING FROM " + nickname); //TODO
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}