package network.ping;

import util.Configurator;

/**
 * Class that continuously sends ping messages
 */
public class PingSender extends Thread {

    private final NetworkHandler networkHandler;
    private final boolean isServer;

    /**
     * Constructor: build a PingSender
     *
     * @param networkHandler The sender
     * @param isServer       True if the sender is a server
     */
    public PingSender(NetworkHandler networkHandler, boolean isServer) {
        this.networkHandler = networkHandler;
        this.isServer = isServer;
    }

    /**
     * Sends ping messages at regular intervals
     */
    public void run() {
        while (networkHandler.isConnected()) {
            if (Configurator.getPingFlag()) {
                System.out.println("\n\t>Sending ping");
            }
            networkHandler.send(new PingMessage(isServer));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {
            }

        }
    }
}