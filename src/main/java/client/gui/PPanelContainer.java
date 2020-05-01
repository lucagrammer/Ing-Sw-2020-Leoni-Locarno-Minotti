package client.gui;

import javax.swing.*;
import java.awt.*;

/**
 * A transparent panel
 */
public class PPanelContainer extends JPanel {

    /**
     * Constructor: build a transparent panel
     */
    public PPanelContainer() {
        setBackground(new Color(0, 0, 0, 0));
        setOpaque(false);
        setLayout(null);
    }

    public void paintComponent(Graphics g) {
        g.setColor(getBackground());
        Rectangle r = g.getClipBounds();
        g.fillRect(r.x, r.y, r.width, r.height);
        super.paintComponent(g);
    }
}
