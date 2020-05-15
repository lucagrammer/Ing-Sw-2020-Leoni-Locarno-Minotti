package client.guiComponents;

import javax.swing.*;
import java.awt.*;

/**
 * A button with a background image and a sign over it
 */
public class PButtonSigned extends PButton{
    private final String sign;

    /**
     * Constructor: build a button with a background image and a sign over it
     * @param image The background image
     * @param sign  The text to be displayed
     */
    public PButtonSigned(Image image, String sign){
        super(image);
        this.sign=sign;
        setText(sign);
        setFont(new Font("LeGourmetScript", Font.PLAIN, 30));
        setForeground(Color.WHITE);
        setHorizontalTextPosition(JButton.CENTER);
        setVerticalTextPosition(JButton.CENTER);
    }

    /**
     * Gets the sign of the button
     * @return  The text of the button
     */
    public String getSign() {
        return sign;
    }

}
