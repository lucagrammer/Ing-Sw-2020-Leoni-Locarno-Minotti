package client.guiComponents;

import client.GuiView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

/**
 * A player switcher with an action listener that enable multiple or single selection
 */
public class PlayerSwitcher {
    private final GuiView guiView;
    private final PPanelContainer bodyContainer;

    private final PPanelContainer playerContainer;

    /**
     * Constructor: build a player switcher
     * @param bodyContainer The container of the player switcher
     * @param guiView       The guiView that controls the bodyContainer
     */
    public PlayerSwitcher(PPanelContainer bodyContainer, GuiView guiView){
        this.bodyContainer=bodyContainer;
        this.guiView=guiView;

        // Prepares the external container
        playerContainer = new PPanelContainer();
        playerContainer.setBounds(0, 100, bodyContainer.getWidth(), 200);
        playerContainer.setLayout(new GridLayout(1, 3, 40, 0));
    }

    /**
     * Sets the text of the player switcher heading
     * @param heading   The text to be shown
     */
    public void showHeading(String heading){
        PLabel label = new PLabel(heading);
        label.setFontSize(30);
        label.setBounds(0, 10, bodyContainer.getWidth(), 40);
        bodyContainer.add(label);
    }

    /**
     * Adds the switcher to the container in a default position with a single selection action listener
     * @param playersNicknames      All the players nicknames
     */
    public void showSwitcher(List<String> playersNicknames) {
        bodyContainer.add(playerContainer);
        int index=0;

        for (String player : playersNicknames) {
            Image scaledImage = (new ImageIcon(getClass().getResource("/GuiResources/couple"+index+".png"))).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            PButton choice= new PButton(scaledImage);
            choice.setLayout(null);
            choice.addMouseListener(new PlayerMouseListener(player));
            playerContainer.add(choice);

            PLabel choiceName= new PLabel("<CENTER>"+player+"</CENTER>");
            choiceName.setBounds(0,150,(playerContainer.getWidth()-40*(playersNicknames.size()-1))/playersNicknames.size(),20);
            choiceName.setFontSize(20);
            choice.add(choiceName);
            index++;
        }
    }


    /**
     * The mouse event manager for the player switcher
     */
    public class PlayerMouseListener implements MouseListener {
        private final String playerNickname;

        /**
         * Constructor: build a PlayerMouseListener with single selection enabled
         * @param playerNickname    The player nickname
         */
        public PlayerMouseListener(String playerNickname) {
            this.playerNickname = playerNickname;
        }

        /**
         * Mouse on-card-click manager: update the guiView
         * @param e The mouse event
         */
        public void mouseClicked(MouseEvent e) {
            (new Thread(() -> {
                guiView.showLoading();
                guiView.getServerHandler().sendFirstPlayer(playerNickname);
            })).start();
        }

        /**
         * Mouse on-card-press manager: same as on-card-click
         * @param e The mouse event
         */
        public void mousePressed(MouseEvent e) {
            mouseClicked(e);
        }

        /**
         * Do nothing
         * @param e The mouse event
         */
        public void mouseReleased(MouseEvent e) {}

        /**
         * Do nothing
         * @param e The mouse event
         */
        public void mouseEntered(MouseEvent e) { }

        /**
         * Do nothing
         * @param e The mouse event
         */
        public void mouseExited(MouseEvent e) {}
    }
}
