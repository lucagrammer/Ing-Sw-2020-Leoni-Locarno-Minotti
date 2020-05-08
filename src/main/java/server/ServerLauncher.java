package server;

import model.Game;
import util.Configurator;
import util.Frmt;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Manages the startup phase of the server and the connection set up
 */
public class ServerLauncher {
    private static ServerSocket serverSocket;
    private Game currentGame;
    private VirtualView currentVirtualView;
    private int addedPlayers;

    /**
     * Constructor: build a ServerLauncher
     */
    public ServerLauncher() {
        currentGame = null;
        currentVirtualView = null;
        addedPlayers = 0;
    }

    public static void main(String[] args) {
        ServerLauncher serverLauncher = new ServerLauncher();
        try {
            serverLauncher.launch();
        } catch (Exception e) {
            Frmt.clearServerLog();
            System.out.println(Frmt.color('r', "> Fatal error: Could not start the server."));
        }
    }

    /**
     * ServerLauncher launcher. Starts the server socket and listen for clients requests
     *
     * @throws IOException When the server can't open the ServerSocket
     */
    public void launch() throws IOException {
        Frmt.clearServerLog();
        serverSocket = new ServerSocket(Configurator.getDefaultPort());
        System.out.println(Frmt.color('g', "> Server started successfully."));

        while (true) {
            try {
                initClient();
            } catch (IOException | InterruptedException e) {
                System.out.println(Frmt.color('r', "> Connection Error: Could not accept the connection."));
                e.printStackTrace();
            }
        }
    }

    /**
     * Add a client to an existing game or create a new game if there are no games available
     *
     * @throws IOException          When happens a connection error
     * @throws InterruptedException When the server has been interrupted
     */
    public void initClient() throws IOException, InterruptedException {
        ClientHandler clientHandler;

        Socket socket = serverSocket.accept();
        System.out.println(Frmt.color('g', "> " + socket.getRemoteSocketAddress() + " has connected."));

        // Creates a game if it doesn't exist and add the client to it
        if (currentGame == null) {
            initGame();
            clientHandler = new ClientHandler(currentVirtualView, socket, true);
        } else {
            clientHandler = new ClientHandler(currentVirtualView, socket, false);
        }
        currentVirtualView.addClientHandler(clientHandler);
        clientHandler.start();
        addedPlayers++;

        // Sleep until the number of players of the game has been set
        synchronized (currentGame) {
            while (currentGame.getNumPlayers() == -1 && currentGame.isActive()) {
                currentGame.wait();
            }
        }
        if (addedPlayers == currentGame.getNumPlayers() || !currentGame.isActive()) {
            clearGameRoom();
        }
    }

    /**
     * Clears the game room preparing it to welcome new users
     */
    private void clearGameRoom() {
        System.out.println("> Status: Game room is full.");
        currentGame = null;
        currentVirtualView = null;
        addedPlayers = 0;
    }

    /**
     * Creates and initializes a new game instance
     */
    private void initGame() {
        System.out.println("> Status: New game has been created.");

        // Setup of the server side game management
        currentGame = new Game();
        Controller controller = new Controller(currentGame);
        currentVirtualView = new VirtualView(controller);
        controller.setVirtualView(currentVirtualView);

        // Starts the game controller
        (new Thread(() -> {
            try {
                controller.gameStarter();
            } catch (Exception ignored) {
            }
            System.out.println(Frmt.color('r', "> Status: Controller has stopped."));
        })).start();
    }
}