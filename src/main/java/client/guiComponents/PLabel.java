package client.guiComponents;

import javax.swing.*;
import java.awt.*;

/**
 * A customized centered label with Santorini Font
 */
public class PLabel extends JLabel {

    /**
     * Constructor: build a customized centered label with Santorini Font
     *
     * @param labelText labelText The text of the label
     */
    public PLabel(String labelText) {
        int defaultSize = 40;
        super.setText("<HTML>"+labelText+"</HTML>");
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(JLabel.CENTER);
        setForeground(Color.WHITE);
        setFont(new Font("LeGourmetScript", Font.PLAIN, defaultSize));
    }

    /**
     * Sets the text of the label
     * @param text  The text to be shown
     */
    public void setText(String text) {
        super.setText("<HTML>"+text+"</HTML>");
    }

    /**
     * Sets the font size
     *
     * @param fontSize The font size
     */
    public void setFontSize(int fontSize) {
        setFont(new Font("LeGourmetScript", Font.PLAIN, fontSize));
    }
}