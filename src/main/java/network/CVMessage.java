package network;

import client.View;

/**
 * Message from Controller to View
 */
public interface CVMessage extends Message {

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    void execute(View view);
}
