package Server;

import Messages.ClientToServer.SetUpGame;
import Messages.Message;
import Messages.PingMessage;
import Messages.ServerToClient.SetUpNewNickname;
import Messages.VCMessage;
import Util.Frmt;
import Util.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.Date;

/**
 * Manages communication from and to a client
 */
class ClientHandler extends Thread {
    private final ServerSocket serverSocket;
    private final VirtualView virtualView;
    private final boolean isFirstPlayer;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String nickname;
    private boolean isConnected;
    private Date lastPing;

    /**
     * Constructor: build a ClientHandler
     *
     * @param serverSocket The server socket
     * @param virtualView  The virtual view
     */
    public ClientHandler(ServerSocket serverSocket, VirtualView virtualView, boolean isFirstPlayer) {
        this.serverSocket = serverSocket;
        this.virtualView = virtualView;
        this.isFirstPlayer = isFirstPlayer;
        this.isConnected = false;

        // Temporary username
        StringBuilder builder = new StringBuilder();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 15; i++) {
            int randomChar = (int) (Math.random() * alphabet.length());
            builder.append(alphabet.charAt(randomChar));
        }
        this.nickname = builder.toString();
    }

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
                    if (clientMessage instanceof PingMessage) {
                        lastPing = Date.from(Instant.now());
                    }
                    vcMessage.execute(virtualView);
                }
            } catch (IOException | ClassNotFoundException e) {
                if (isConnected) {
                    System.out.println(Frmt.color('r', "> Warning: could not receive from " + nickname + ": disconnected"));
                    //e.printStackTrace();
                    isConnected = false;
                    virtualView.setDisconnected(nickname);
                } else {
                    System.out.println(Frmt.color('r', "> Status: " + nickname + " was forced to stop "));
                }
            }
        }
    }

    /**
     * Sends a message to the client
     *
     * @param message The message to be sent
     */
    public void send(Message message) {
        try {
            output.writeUnshared(message);
            output.flush();
            output.reset();
        } catch (IOException e) {
            if (isConnected) {
                System.out.println(Frmt.color('r', "> Warning: could not contact" + nickname + ": disconnected"));
                //e.printStackTrace();
                isConnected = false;
                virtualView.setDisconnected(nickname);
            } else {
                System.out.println(Frmt.color('r', "> Status: " + nickname + " was forced to stop "));
            }
        }
    }

    /**
     * Setup player and game information
     */
    private void acceptPlayerConnection() {
        try {
            socket = serverSocket.accept();
            lastPing = Date.from(Instant.now());
            (new Ping()).run();

            isConnected = true;
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            send(new SetUpGame(isFirstPlayer));
            // start thread connessione

            Message response = (Message) input.readObject();
            if (response instanceof SetUpGame) {
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
                            response = (Message) input.readObject();
                            requestedNickname = ((SetUpNewNickname) response).getNickname();
                        }
                    } while (!uniqueUsername);

                    System.out.println(Frmt.color('g', nickname + "> Connected to " + socket.getRemoteSocketAddress() + " as " + requestedNickname.toUpperCase()));
                }
                nickname = requestedNickname;
                virtualView.ready();
            }
        } catch (IOException | ClassNotFoundException e) {
            if (isConnected) {
                System.out.println(Frmt.color('r', "> Warning: connection setup failed (temporary id:" + nickname + "): disconnected"));
                //e.printStackTrace();
                isConnected = false;
                virtualView.setDisconnected(nickname);
            } else {
                System.out.println(Frmt.color('r', "> Status: " + nickname + " was forced to stop "));
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

    public void close() {
        this.isConnected = false;
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println(Frmt.color('r', "> Error:  can't close the socket of " + nickname));
            //e.printStackTrace();
        }
    }


    class Ping extends Thread {

        public void run() {
            while (isConnected) {
                send(new PingMessage(true));

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Date now = Date.from(Instant.now());
                long timeDifference = now.getTime() - lastPing.getTime();
                if (timeDifference > 10000) {
                    isConnected = false;
                    return;
                }
            }
        }
    }

}
