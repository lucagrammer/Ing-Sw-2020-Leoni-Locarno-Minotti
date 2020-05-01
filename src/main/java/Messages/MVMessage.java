package Messages;

import client.View;

/**
 * Message from Model to View
 */
public interface MVMessage extends Message {

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    void execute(View view);
}
