package Server;

import Messages.ClientToServer.ConnectionSetup;
import Messages.Message;
import Messages.ServerToClient.ResetNicknameProcess;
import Messages.VCMessage;
import Util.Formatter;
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
    private Socket socket;
    private ServerSocket serverSocket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String nickname;
    private VirtualView virtualView;
    private boolean isConnected;
    private boolean isFirstPlayer;

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
                    vcMessage.execute(virtualView);
                }
            } catch (IOException | ClassNotFoundException e) {
                isConnected = false;
                System.out.println(Formatter.cText('r', "> Warning: " + nickname + " has disconnected"));
                e.printStackTrace();
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
            System.out.println(Formatter.cText('r', "> Error: Could not contact the client " + socket.getRemoteSocketAddress()));
            e.printStackTrace();
        }
    }

    /**
     * Setup player and game information
     */
    private void acceptPlayerConnection() {
        try {
            socket = serverSocket.accept();
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            send(new ConnectionSetup(isFirstPlayer));

            Message response = (Message) input.readObject();
            if (response instanceof ConnectionSetup) {
                ConnectionSetup connectionSetup = (ConnectionSetup) response;
                String requestedNickname = connectionSetup.getNickname();
                Date birthDate = connectionSetup.getBirthDate();

                if (isFirstPlayer) {
                    System.out.println(Formatter.cText('g', "> Connected to " + socket.getRemoteSocketAddress() + " as " + requestedNickname.toUpperCase()));

                    int playersNumber = connectionSetup.getNumPlayers();
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
                            System.out.println(Formatter.cText('y', "> Warning: " + socket.getRemoteSocketAddress() + " is trying to connect with a nickname already in use"));
                            send(new ResetNicknameProcess());
                            response = (Message) input.readObject();
                            requestedNickname = ((ResetNicknameProcess) response).getNickname();
                        }
                    } while (!uniqueUsername);

                    System.out.println(Formatter.cText('g', "> Connected to " + socket.getRemoteSocketAddress() + " as " + requestedNickname.toUpperCase()));
                }
                isConnected = true;
                nickname = requestedNickname;
                virtualView.ready();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(Formatter.cText('r', "> Error: connection rejected"));
            e.printStackTrace();
        }
    }

    /**
     * Closes the socket
     */
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println(Formatter.cText('r', "> Error: An error occurred when closing the connection " + nickname));
            e.printStackTrace();
        }
    }
}
