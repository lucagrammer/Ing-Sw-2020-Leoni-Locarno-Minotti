package Client;

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
                    //view.addServerHandler(serverHandler);
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
}
