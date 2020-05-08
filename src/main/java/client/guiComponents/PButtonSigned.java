package client.guiComponents;

import javax.swing.*;
import java.awt.*;

public class PButtonSigned extends PButton{
    private String sign;

    public PButtonSigned(Image image, String sign){
        super(image);
        this.sign=sign;
        setText(sign);
        setFont(new Font("LeGourmetScript", Font.PLAIN, 27));
        setForeground(Color.WHITE);
        setHorizontalTextPosition(JButton.CENTER);
        setVerticalTextPosition(JButton.CENTER);
    }

    public String getSign() {
        return sign;
    }

}
