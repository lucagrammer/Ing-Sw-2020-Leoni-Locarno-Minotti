package Server;

import Util.Configurator;
import Util.Frmt;
import Util.exceptions.MustRestartException;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Manages the initial startup phase of the server
 */
public class ServerLauncher {
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        try {
            launch();
        } catch (Exception e) {
            Frmt.clearServerLog();
            System.out.println(Frmt.color('r', "> Fatal error: An unknown error has occurred. Server has been restarted."));
            e.printStackTrace();
            if (drop()) {
                launch();
            } else {
                System.out.println(Frmt.color('r', "> Fatal error: Server cannot be restarted"));
            }
        }
    }

    /**
     * ServerLauncher launcher. Starts the server socket.
     */
    public static void launch() {
        Frmt.clearServerLog();
        try {
            serverSocket = new ServerSocket(Configurator.getDefaultPort());
        } catch (IOException e) {
            System.out.println(Frmt.color('r', "> Fatal error: Could not start the server"));
            //e.printStackTrace();
            return;
        }
        System.out.println(Frmt.color('g', "> Server started successfully"));

        init();
    }

    /**
     * Initializes the game objects
     */
    public static void init() {
        // Setup all the objects that the server will use
        Controller controller = new Controller();
        VirtualView virtualView = new VirtualView(controller);
        controller.setVirtualView(virtualView);

        System.out.println("> Status: Waiting for the first player to connect");
        ClientHandler clientHandler = new ClientHandler(serverSocket, virtualView, true);
        virtualView.addClientHandler(clientHandler);
        clientHandler.start();

        try {
            controller.gameStarter();
        } catch (InterruptedException e) {
            System.out.println(Frmt.color('r', "> Fatal error: Server stopped suddenly"));
            e.printStackTrace();
        } catch (MustRestartException e) {
            // Game must be re-initialized: drop and restart
            Frmt.clearServerLog();
            if (drop()) {
                launch();
            } else {
                System.out.println(Frmt.color('r', "> Fatal error: Server cannot be restarted"));
            }
        }
    }

    /**
     * Drops the server connection
     *
     * @return True if the operation was successful, otherwise false
     */
    public static boolean drop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
