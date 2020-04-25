package Server;

import Messages.ClientToServer.SetUpGame;
import Messages.Message;
import Messages.PingMessage;
import Messages.ServerToClient.SetUpNewNickname;
import Messages.VCMessage;
import Util.Configurator;
import Util.Frmt;
import Util.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * Manages communication from and to a client
 */
class ClientHandler extends Thread {
    private final ServerSocket serverSocket;
    private final VirtualView virtualView;
    private final boolean isFirstPlayer;
    private final Object lock = new Object();
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String nickname;
    private boolean isConnected;
    private boolean isDaemonHandler;

    /**
     * Constructor: build a ClientHandler
     *
     * @param serverSocket  The server socket
     * @param virtualView   The virtual view
     * @param isFirstPlayer True if the player will be the first player, otherwise false
     */
    public ClientHandler(ServerSocket serverSocket, VirtualView virtualView, boolean isFirstPlayer) {
        this.serverSocket = serverSocket;
        this.virtualView = virtualView;
        this.isFirstPlayer = isFirstPlayer;
        this.isConnected = false;
        this.isDaemonHandler = true;

        // Temporary username
        StringBuilder builder = new StringBuilder();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 15; i++) {
            int randomChar = (int) (Math.random() * alphabet.length());
            builder.append(alphabet.charAt(randomChar));
        }
        this.nickname = builder.toString();
    }

    /**
     * Gets the Nickname of the connected user
     *
     * @return The nickname of the connected user
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * As long as the client is connected, waits for a message from the client and execute it
     */
    public void run() {
        if (!isConnected) {
            acceptPlayerConnection();
        }

        while (isConnected) {
            try {
                Message clientMessage = (Message) input.readObject();
                if (clientMessage.getType() == MessageType.VC) {
                    VCMessage vcMessage = (VCMessage) clientMessage;
                    if (clientMessage instanceof PingMessage && Configurator.getPingFlag()) {
                        System.out.println("Received ping from " + nickname);
                    }
                    vcMessage.execute(virtualView);
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
     * Sends a message to the client
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
     * Setup player and game information
     */
    private void acceptPlayerConnection() {
        try {
            socket = serverSocket.accept();
            isDaemonHandler = false;

            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            isConnected = true;

            // Sets the connection timeout to 20 seconds
            socket.setSoTimeout(20000);
            // Start sending pings to the client every 5 seconds
            (new PingSender()).start();

            send(new SetUpGame(isFirstPlayer));

            boolean isGameSetUp = false;
            while (!isGameSetUp) {
                Message response = (Message) input.readObject();
                if (response instanceof SetUpGame) {
                    isGameSetUp = true;
                    SetUpGame setupGame = (SetUpGame) response;
                    String requestedNickname = setupGame.getNickname();
                    Date birthDate = setupGame.getBirthDate();

                    if (isFirstPlayer) {
                        System.out.println(Frmt.color('g', nickname + "> Connected to " + socket.getRemoteSocketAddress() + " as " + requestedNickname.toUpperCase()));

                        int playersNumber = setupGame.getNumPlayers();
                        virtualView.requestAddPlayer(requestedNickname, birthDate, playersNumber);
                        System.out.println("> Status: Waiting for the other players to connect");
                        for (int i = 0; i < playersNumber - 1; i++) {
                            ClientHandler clientHandler = new ClientHandler(serverSocket, virtualView, false);
                            virtualView.addClientHandler(clientHandler);
                            clientHandler.start();
                        }
                    } else {
                        boolean uniqueUsername;
                        do {
                            uniqueUsername = virtualView.requestAddPlayer(requestedNickname, birthDate);
                            if (!uniqueUsername) {
                                System.out.println(Frmt.color('y', "> Warning: " + socket.getRemoteSocketAddress() + " is trying to connect with a nickname already in use"));
                                send(new SetUpNewNickname());

                                boolean hasRecievedNewNickname = false;
                                while (!hasRecievedNewNickname) {
                                    response = (Message) input.readObject();
                                    if (response instanceof SetUpNewNickname) {
                                        hasRecievedNewNickname = true;
                                        requestedNickname = ((SetUpNewNickname) response).getNickname();
                                    }
                                    if (response instanceof PingMessage && Configurator.getPingFlag()) {
                                        System.out.println("Received ping from " + nickname);
                                    }
                                }

                            }
                        } while (!uniqueUsername);

                        System.out.println(Frmt.color('g', nickname + "> Connected to " + socket.getRemoteSocketAddress() + " as " + requestedNickname.toUpperCase()));
                    }
                    nickname = requestedNickname;
                    virtualView.ready();
                }
                if (response instanceof PingMessage && Configurator.getPingFlag()) {
                    System.out.println("Received ping from " + nickname);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            if (isConnected) {
                // This player has disconnected
                System.out.println(Frmt.color('r', "> Warning: " + nickname + " has disconnected during connection setup"));
                isConnected = false;
                virtualView.setDisconnected(nickname);
                close();
            } else {
                // Another player has disconnected
                System.out.println(Frmt.color('r', "> Status: " + nickname + " was forced to stop during connection setup "));
            }
        }
    }

    /**
     * Tests the clientHandler connection status
     *
     * @return True if the clientHandler is connected, otherwise false
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
     * Tests if the clientHandler has ever had a user connected
     *
     * @return True if the clientHandler has ever had a user connected, otherwise false
     */
    public boolean isDaemonHandler() {
        return isDaemonHandler;
    }

    /**
     * Closes the socket connection
     */
    public void close() {
        this.isConnected = false;
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println(Frmt.color('r', "> Error:  can't close the socket of " + nickname));
        }
    }


    /**
     * Class that continuously sends ping messages to the client
     */
    class PingSender extends Thread {

        /**
         * Sends ping messages at regular intervals
         */
        public void run() {
            while (isConnected) {
                if (Configurator.getPingFlag()) {
                    System.out.println("\n\t>Sending ping to " + nickname);
                }
                send(new PingMessage(true));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.println(Frmt.color('r', "> Error:  Ping timer error " + socket.getRemoteSocketAddress() + " (" + nickname + ")"));
                    e.printStackTrace();
                }

            }
        }
    }

}
