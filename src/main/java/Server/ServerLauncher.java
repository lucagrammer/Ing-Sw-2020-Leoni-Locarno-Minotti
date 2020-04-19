package Server;

import Util.Configurator;
import Util.Frmt;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Manages the initial startup phase of the server
 */
public class ServerLauncher {
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        ServerLauncher serverLauncher = new ServerLauncher();
        serverLauncher.launch();
    }

    public static void newGame() {
        Controller controller = new Controller();
        VirtualView virtualView = new VirtualView();
        controller.setVirtualView(virtualView);
        virtualView.setController(controller);

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

    /**
     * ServerLauncher launcher. Starts the first ClientHandler thread.
     */
    public void launch() {
        try {
            serverSocket = new ServerSocket(Configurator.getDefaultPort());
        } catch (IOException e) {
            System.out.println(Frmt.color('r', "> Error: Could not start the server"));
            //e.printStackTrace();
            return;
        }
        System.out.println(Frmt.color('g', "> Server started successfully"));
        newGame();
    }
}
