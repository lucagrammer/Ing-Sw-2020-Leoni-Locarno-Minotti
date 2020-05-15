package client.gui.components;

import java.awt.*;

/**
 * A customized centered state label with Santorini Font
 */
public class PLabelState extends PLabel {
    /**
     * Constructor: build a customized centered state label with Santorini Font
     *
     * @param stateMessage The text of the state label
     */
    public PLabelState(String stateMessage) {
        super(stateMessage);
        setFont(new Font("LeGourmetScript", Font.PLAIN, 25));
    }

    /**
     * Sets the foreground color to the error-style color
     */
    public void setErrorForeground() {
        setForeground(new Color(255, 83, 83, 255));
    }

    /**
     * Sets the foreground color to the message-style color
     */
    public void setMessageForeground() {
        setForeground(Color.WHITE);
    }
}