package client.gui.components;

import javax.swing.*;
import java.awt.*;

/**
 * A customized form label with Santorini Font
 */
public class PLabelForm extends PLabel {

    /**
     * Constructor: build a customized form label with Santorini Font
     *
     * @param labelText The text of the error label
     */
    public PLabelForm(String labelText) {
        super(labelText);

        setHorizontalAlignment(SwingConstants.RIGHT);
        setFont(new Font("LeGourmetScript", Font.PLAIN, 30));
    }
}