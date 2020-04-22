package Messages.ServerToClient;

import Client.View;
import Messages.CVMessage;
import Messages.VCMessage;
import Server.VirtualView;
import Util.MessageType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Message to manage the choice of the first player by the challenger
 */
public class SetUpFirstPlayer implements Serializable, CVMessage, VCMessage {
    private final MessageType messageType;
    private List<String> nicknames;
    private String firstPlayerNickname;

    /**
     * Server-side constructor: build a request message
     *
     * @param nicknames All the nicknames
     */
    public SetUpFirstPlayer(List<String> nicknames) {
        messageType = MessageType.CV;
        this.nicknames = new ArrayList<>(nicknames);
    }

    /**
     * Client-side constructor: build a response message
     *
     * @param fistPlayerNickname The nickname of the chosen first player
     */
    public SetUpFirstPlayer(String firstPlayerNickname) {
        messageType = MessageType.VC;
        this.firstPlayerNickname = firstPlayerNickname;
    }

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    public void execute(View view) {
        view.askFirstPlayer(nicknames);
    }

    /**
     * Execute the request server-side
     *
     * @param virtualView The recipient component
     */
    public void execute(VirtualView virtualView) {
        virtualView.setFirstPlayer(firstPlayerNickname);
    }

    /**
     * Gets the message type
     *
     * @return The message type
     */
    public MessageType getType() {
        return messageType;
    }
}
