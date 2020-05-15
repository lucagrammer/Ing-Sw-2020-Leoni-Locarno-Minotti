package network.messages;

import client.View;
import network.CVMessage;
import util.MapInfo;
import util.MessageType;

import java.io.Serializable;

/**
 * Message to show the updated game map to the user
 */
public class ShowMap implements CVMessage, Serializable {
    private final MessageType messageType;
    private final MapInfo mapInfo;
    private final String currentNickname;
    private final String loserNickname;

    /**
     * Server-side constructor: build a request message
     *
     * @param currentNickname The nickname of the current player
     * @param mapInfo         The map info
     * @param loserNickname   The nickname of the loser player or null value
     */
    public ShowMap(MapInfo mapInfo, String currentNickname, String loserNickname) {
        messageType = MessageType.CV;
        this.currentNickname = currentNickname;
        this.loserNickname = loserNickname;
        this.mapInfo = mapInfo;
    }

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    public void execute(View view) {
        view.showMap(mapInfo, true);
        if (loserNickname != null) {
            view.showLoser(loserNickname);
        }
        view.showTurn(currentNickname, loserNickname == null);
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