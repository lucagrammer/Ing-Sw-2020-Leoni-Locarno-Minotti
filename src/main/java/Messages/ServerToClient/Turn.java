package Messages.ServerToClient;
import Client.View;
import Messages.CVMessage;
import Messages.VCMessage;
import Server.VirtualView;
import Util.Action;
import Util.MessageType;
import Util.RoundActions;
import java.io.Serializable;
public class Turn implements Serializable, CVMessage, VCMessage {
    private MessageType messageType;
    private RoundActions roundActions;
    private Action action;
    private String nickname;
    /**
     * Server-side constructor: build a request message
     *
     */
    public Turn(RoundActions roundActions) {
        messageType = MessageType.CV;
        this.roundActions=roundActions;
    }
    /**
     * Client-side constructor: build a response message
     *
     */
    public Turn(Action action, String nickname) {
        messageType = MessageType.VC;
        this.action = action;
        this.nickname=nickname;
    }
    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    public void execute(View view) {
        view.askAction(roundActions);}
    /**
     * Execute the request server-side
     *
     * @param virtualView The recipient component
     */
    public void execute(VirtualView virtualView) {
        virtualView.setAction(action,nickname);
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