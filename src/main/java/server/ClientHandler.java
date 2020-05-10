package server;

import network.Message;
import network.SYSMessage;
import network.VCMessage;
import network.messages.SetUpGame;
import network.messages.ShowDisconnection;
import network.ping.NetworkHandler;
import network.ping.PingMessage;
import network.ping.PingSender;
import util.Configurator;
import util.Frmt;
import util.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Manages communication from and to a client
 */
class ClientHandler extends Thread implements NetworkHandler {
    private final VirtualView virtualView;
    private final boolean isFirstPlayer;
    private final Object lock = new Object();
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    private final Socket socket;
    private String nickname;
    private boolean isConnected;

    /**
     * Constructor: build a ClientHandler
     *
     * @param virtualView   The virtual view
     * @param socket        The socket
     * @param isFirstPlayer True if the player will be the first player, otherwise false
     * @throws IOException When it's not possible to open input or output streams
     */
    public ClientHandler(VirtualView virtualView, Socket socket, boolean isFirstPlayer) throws IOException {
        this.virtualView = virtualView;
        this.isFirstPlayer = isFirstPlayer;
        this.socket = socket;
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
        this.isConnected = true;

        // Sets the connection timeout to 20 seconds and start sending pings to the client every 5 seconds
        socket.setSoTimeout(20000);
        (new PingSender(this, true)).start();

        // Temporary username
        StringBuilder builder = new StringBuilder();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 25; i++) {
            int randomChar = (int) (Math.random() * alphabet.length());
            builder.append(alphabet.charAt(randomChar));
        }
        this.nickname = builder.toString();
    }

    /**
     * Gets the nickname of the connected user
     *
     * @return The nickname of the connected user
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the final nickname of the connected user
     *
     * @param nickname The new nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * As long as the client is connected, waits for a message from the client and execute it
     */
    public void run() {
        connectionSetUp();

        while (isConnected) {
            try {
                Message clientMessage = (Message) input.readObject();
                if (clientMessage.getType() == MessageType.VC) {
                    if (clientMessage instanceof PingMessage && Configurator.getPingFlag()) {
                        System.out.println("Received ping from " + nickname);
                    }
                    VCMessage vcMessage = (VCMessage) clientMessage;
                    vcMessage.execute(virtualView);
                }
                if (clientMessage.getType() == MessageType.SYS) {
                    manageSystemMessage(clientMessage);
                }
            } catch (IOException | ClassNotFoundException e) {
                if (isConnected) {
                    // This player has disconnected
                    System.out.println(Frmt.color('r', "> Warning: " + nickname + " has disconnected during message receiving"));
                    isConnected = false;
                    virtualView.setDisconnected(nickname);
                } else {
                    // Another player has disconnected
                    System.out.println(Frmt.color('r', "> Status: " + nickname + " was forced to stop during message receiving"));
                }
                close();
            }
        }
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
                if (isConnected) {
                    // This player has disconnected
                    System.out.println(Frmt.color('r', "> Warning: " + nickname + " has disconnected during message sending"));
                    isConnected = false;
                    virtualView.setDisconnected(nickname);
                } else {
                    // Another player has disconnected
                    System.out.println(Frmt.color('r', "> Status: " + nickname + " was forced to stop during message sending"));
                }
                close();
            }
        }
    }

    /**
     * Sends the initial connectionSetUp message to the client
     */
    private void connectionSetUp() {
        send(new SetUpGame(isFirstPlayer, nickname));
    }

    /**
     * Manages system messages for game setup
     */
    private void manageSystemMessage(Message clientMessage) {
        if (virtualView.checkGameStatus()) {
            SYSMessage sysMessage = (SYSMessage) clientMessage;
            sysMessage.execute(virtualView, nickname);
        } else {
            send(new ShowDisconnection("Another player"));
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
     * Sets the clientHandler status as disconnected
     */
    public void setDisconnected() {
        this.isConnected = false;
    }

    /**
     * Closes the connection to the client
     */
    public void close() {
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }
}
