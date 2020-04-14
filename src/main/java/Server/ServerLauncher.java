package Server;

import Util.Configurator;
import Util.Frmt;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Manages the initial startup phase of the server
 */
public class ServerLauncher {
    private final VirtualView virtualView;
    private final Controller controller;

    /**
     * Constructor: build the ServerLauncher
     */
    public ServerLauncher() {
        controller = new Controller();
        virtualView = new VirtualView();
        controller.setVirtualView(virtualView);
        virtualView.setController(controller);
    }

    public static void main(String[] args) {
        ServerLauncher serverLauncher = new ServerLauncher();
        serverLauncher.launch();
    }

    /**
     * ServerLauncher launcher. Starts the first ClientHandler thread.
     */
    public void launch() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(Configurator.getDefaultPort());
        } catch (IOException e) {
            System.out.println(Frmt.color('r', "> Error: Could not start the server"));
            e.printStackTrace();
            return;
        }
        System.out.println(Frmt.color('g', "> Server started successfully"));

        System.out.println("> Status: Waiting for the first player to connect");
        ClientHandler clientHandler = new ClientHandler(serverSocket, virtualView, true);
        virtualView.addClientHandler(clientHandler);
        clientHandler.start();

        try {
            controller.gameStarter();
        } catch (InterruptedException e) {
            System.out.println(Frmt.color('r', "> Error: Server can't start the game"));
            e.printStackTrace();
        }
    }
}
