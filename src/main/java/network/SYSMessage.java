package network;

import server.VirtualView;

/**
 * System message to manage exceptional situations
 */
public interface SYSMessage extends Message {

    /**
     * Execute the request server-side
     *
     * @param virtualView       The recipient component
     * @param temporaryUsername The temporary username of the user
     */
    void execute(VirtualView virtualView, String temporaryUsername);
}