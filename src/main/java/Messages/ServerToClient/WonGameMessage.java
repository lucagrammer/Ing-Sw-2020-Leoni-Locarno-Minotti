package Messages.ServerToClient;

import Client.View;
import Messages.MVMessage;
import Util.MessageType;

import java.io.Serializable;

/**
 * Message to notify the winner and to notify the end of the game
 */
public class WonGameMessage implements Serializable, MVMessage {
    private final MessageType messageType;
    private final String winnerNickname;
    private final boolean youWin;

    /**
     * Server-side constructor: build a request message
     *
     * @param winnerNickname The nickname of the winner
     * @param youWin         True if the recipient is the winner
     */
    public WonGameMessage(String winnerNickname, boolean youWin) {
        messageType = MessageType.MV;
        this.winnerNickname = winnerNickname;
        this.youWin = youWin;
    }

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    public void execute(View view) {
        view.showGameEnd(winnerNickname, youWin);
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
