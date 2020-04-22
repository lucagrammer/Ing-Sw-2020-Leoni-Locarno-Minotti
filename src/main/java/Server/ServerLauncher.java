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
        launch();
    }

    /**
     * ServerLauncher launcher. Starts the first ClientHandler thread.
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
            // Game must be re-initialized
            Frmt.clearServerLog();
            init();
        }
    }
}
