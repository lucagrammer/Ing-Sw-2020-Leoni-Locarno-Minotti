package client.guiComponents;

import java.awt.*;

/**
 * A customized centered error label with Santorini Font
 */
public class PLabelError extends PLabel {
    /**
     * Constructor: build a customized centered error label with Santorini Font
     *
     * @param errorMessage The text of the error label
     */
    public PLabelError(String errorMessage) {
        super(errorMessage);
        setFont(new Font("LeGourmetScript", Font.PLAIN, 25));
        setForeground(Color.RED);
    }
}