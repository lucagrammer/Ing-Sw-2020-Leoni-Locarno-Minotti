package client.gui;

import javax.swing.*;
import java.awt.*;

/**
 * A customized centered label with Santorini Font
 */
public class PLabel extends JLabel {
    /**
     * Constructor: build a customized centered label with Santorini Font
     *
     * @param labelText The text of the label
     */
    public PLabel(String labelText) {
        int defaultSize = 40;
        setText(labelText);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(JLabel.CENTER);
        setForeground(Color.WHITE);
        setFont(new Font("LeGourmetScript", Font.PLAIN, defaultSize));
    }

    public void setFontSize(int fontSize) {
        setFont(new Font("LeGourmetScript", Font.PLAIN, fontSize));
    }
}