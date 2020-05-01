package client.gui;

import javax.swing.*;
import java.awt.*;

public class PPanelBackground extends JPanel {

    Image image;

    public PPanelBackground(Image image) {
        this.image = image;
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}