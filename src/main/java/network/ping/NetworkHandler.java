package network.ping;

import network.Message;

/**
 * An agent who interacts with the network
 */
public interface NetworkHandler {

    /**
     * Checks the connection status
     *
     * @return True if it is connected, otherwise false
     */
    boolean isConnected();

    /**
     * Sends a message
     *
     * @param message The message to be sent
     */
    void send(Message message);
}
