package Client.gui;

import javax.swing.*;
import java.awt.*;

public class PanelWithBackground extends JPanel {

    Image image;

    public PanelWithBackground(Image image) {
        this.image = image;
    }

    public static void main(String[] args) {
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}