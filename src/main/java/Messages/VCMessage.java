package Messages;

import Server.VirtualView;

/**
 * Message from View to Controller
 */
public interface VCMessage extends Message {

    /**
     * Execute the request server-side
     *
     * @param virtualView The recipient component
     */
    void execute(VirtualView virtualView);
}
