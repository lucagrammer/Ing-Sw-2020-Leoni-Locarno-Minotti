package Client;

import Messages.CVMessage;
import Messages.ClientToServer.ConnectionSetup;
import Messages.MVMessage;
import Messages.Message;
import Messages.ServerToClient.ChooseCards;
import Messages.ServerToClient.ResetNicknameProcess;
import Messages.ServerToClient.SelectCard;
import Model.Card;
import Util.Configurator;
import Util.Formatter;
import Util.MessageType;

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

    /**
     * Starts listening for server messages and execute them client-side
     */
    public void startListening() {
        boolean running = true;
        while (running) {
            try {
                Message serverMessage = (Message) input.readObject();
                if (serverMessage.getType() == MessageType.MV) {
                    MVMessage mvMessage = (MVMessage) serverMessage;
                    mvMessage.execute(view);
                } else {
                    if (serverMessage.getType() == MessageType.CV) {
                        CVMessage cvMessage = (CVMessage) serverMessage;
                        cvMessage.execute(view);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(Formatter.cText('r', "> Error: Could not contact the server"));
                e.printStackTrace();
                running = false;
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
     * Sets connection to the specified server
     *
     * @param serverIP IP address of the server
     */
    public void setConnection(String serverIP) {
        try {
            socket = new Socket(serverIP, Configurator.getDefaultPort());
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            startListening();
        } catch (IOException e) {
            System.out.println(Formatter.cText('r', "> Error: Server unreachable."));
            e.printStackTrace();
        }
    }

    /**
     * Closes the connection to the server
     */
    public void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println(Formatter.cText('r', "Error: An error occurred when closing the connection"));
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to connected the server
     *
     * @param message The message to be sent
     */
    public void send(Message message) {
        try {
            output.writeUnshared(message);
            output.flush();
            output.reset();
        } catch (IOException e) {
            System.out.println(Formatter.cText('r', "> Error: Could not contact the server"));
            e.printStackTrace();
        }
    }

    /**
     * Prepares the request of ConnectionSetup with information about the player and the game
     *
     * @param nickname   The requested username
     * @param date       The date of birth
     * @param numPlayers The number of players of the game to be created (irrelevant if the player is joining an existing game)
     */
    public void sendGameInfo(String nickname, Date date, int numPlayers) {
        send(new ConnectionSetup(nickname, date, numPlayers));
    }

    /**
     * Prepares the response to the server ResetUsernameProcess request
     *
     * @param newNickname The new requested nickname
     */
    public void sendNewNickname(String newNickname) {
        send(new ResetNicknameProcess(newNickname));
    }

    public void sendCards(List<Card> chosenCards) {
        send(new ChooseCards(chosenCards));
    }

    public void sendCard(Card chosenCard, String nickname) {
        send(new SelectCard(chosenCard, nickname));
    }
}