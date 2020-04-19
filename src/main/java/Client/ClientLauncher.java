package Client;

import Util.Configurator;
import Util.Frmt;

import java.util.Scanner;

/**
 * Manages the initial startup phase of the client
 */
public class ClientLauncher {
    public static void main(String[] args) {
        ClientLauncher clientLauncher = new ClientLauncher();
        clientLauncher.launch();
    }

    /**
     * ClientLauncher launcher. Asks the preferred UI and launches it.
     */
    private void launch() {
        Scanner scanner = new Scanner(System.in);
        ServerHandler serverHandler = new ServerHandler();
        View view;

        Frmt.clearScreen();
        boolean incorrect;
        do {
            System.out.print(Frmt.style('b', " Choose the interface you want to use [CLI/GUI]: "));
            String preferredInterface = scanner.nextLine();
            fastStart(preferredInterface);
            if ((preferredInterface.equalsIgnoreCase("CLI"))) {
                view = new CliView();
                serverHandler.setView(view);
                view.setServerHandler(serverHandler);
                incorrect = false;
                view.launch();
            } else {
                if ((preferredInterface.equalsIgnoreCase("GUI"))) {
                    //view = new CliView();
                    //serverHandler.addView(view);
                    //view.addServerHandeler(serverHandler);
                    //incorrect = false;
                    //view.launch();
                    Frmt.clearScreen();
                    System.out.println(Frmt.color('r', "  > This functionality is not available. Try again."));
                    incorrect = true;
                } else {
                    Frmt.clearScreen();
                    System.out.println(Frmt.color('r', "  > Invalid choice. Try again."));
                    incorrect = true;
                }
            }
        } while (incorrect);
    }

    /**
     * DEBUG ONLY: Apply default settings if the flag fast-start is enabled
     *
     * @param preferredInterface The shortcut to be verified to enable the fastStart-mode
     */
    private void fastStart(String preferredInterface) {
        // Debug mode?
        if (!Configurator.getFastStartFlag()) {
            return;
        }
        if (preferredInterface.equalsIgnoreCase("")) {
            View view = new CliView();
            ServerHandler serverHandler = new ServerHandler();
            serverHandler.setView(view);
            view.setServerHandler(serverHandler);
            serverHandler.setConnection(Configurator.getDefaultIp());
            System.out.println("Exiting FAST_MODE");
        }
    }
}
