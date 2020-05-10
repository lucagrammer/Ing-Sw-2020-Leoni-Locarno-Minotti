package client.guiComponents;

import util.Configurator;
import util.MapInfo;

import javax.swing.*;
import java.awt.*;

/**
 * A transparent button with an image
 */
public class PButton extends JButton {

    /**
     * Constructor: build a transparent button with an image
     *
     * @param image The image of the button
     */
    public PButton(Image image) {
        super(new ImageIcon(image));
        setBackground(new Color(0, 0, 0, 0));
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
    }

    /**
     * Constructor: build a colored button
     *
     * @param color The color of the button
     */
    public PButton(Color color) {
        super();
        setBackground(color);
        setOpaque(true);
        setBorderPainted(false);
    }


    /**
     * Constructor: build a transparent button
     *
     * //TODO
     * @param mapInfo   The map info
     */
    public PButton(MapInfo mapInfo, int row, int column) {
        super();
        setBackground(new Color(0, 0, 0, 0));
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setContent(mapInfo,row,column);
    }

    public void setContent(MapInfo mapInfo, int row, int column){
        int floor = mapInfo.getFloorAt(row,column);
        boolean dome = mapInfo.getDomeAt(row,column);
        String color = mapInfo.getColorAt(row,column); // maybe null
        String state;
        if(color==null){
            state=dome? "dome" : "free";
        }else{
            state=color;
        }

        state="yellow";//TODO
        floor=3;
        setIcon(new ImageIcon(Configurator.getCellImage(floor,state)));
    }
}
