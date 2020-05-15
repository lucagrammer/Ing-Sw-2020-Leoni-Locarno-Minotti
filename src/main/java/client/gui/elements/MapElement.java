package client.gui.elements;

import client.GuiView;
import client.gui.components.*;
import model.Cell;
import util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The map element with board, commands and legend section
 */
public class MapElement {
    private final GuiView guiView;
    private final PPanelContainer mapContainer;
    private final PLabelState messageLabel;
    private final PLabel commandLabel;
    private PPanelContainer commandBar;
    private PPanelContainer keyContainer;
    private PPanelContainer keyNameContainer;
    private PButton[][] cells;
    private ArrayList<PButton> actionButtons;
    private boolean commandsVisibility;

    /**
     * Constructor: build a MapElement
     *
     * @param bodyContainer The container of the player switcher
     * @param guiView       The guiView that controls the bodyContainer
     */
    public MapElement(PPanelContainer bodyContainer, GuiView guiView) {
        this.guiView = guiView;
        this.commandsVisibility = false;

        Image islandImg = (new ImageIcon(getClass().getResource("/GuiResources/SantoriniBoard.png"))).getImage();
        JPanel island = new PPanelBackground(islandImg);
        island.setBounds(0, 80, 496, 500);
        island.repaint();
        island.setLayout(null);
        bodyContainer.add(island);

        mapContainer = new PPanelContainer();
        mapContainer.setBounds(70, 63, 368, 368);
        mapContainer.setLayout(new GridLayout(5, 5, 11, 11));
        island.add(mapContainer);

        commandLabel = new PLabel("");
        commandLabel.setFontSize(30);
        commandLabel.setBounds(0, 0, bodyContainer.getWidth(), 40);
        bodyContainer.add(commandLabel);

        messageLabel = new PLabelState("");
        messageLabel.setBounds(0, 50, 840, 25);
        bodyContainer.add(messageLabel);

        PPanelContainer sideBar = new PPanelContainer();
        sideBar.setBounds(island.getWidth() + 20, 80, 324, 500);
        sideBar.setLayout(null);
        bodyContainer.add(sideBar);

        setUpSideBar(sideBar);
        prepareActionButtons();
    }

    /**
     * Prepares the key containers and the commands container adding them to the sidebar of the mapElement
     *
     * @param sideBar The sidebar
     */
    private void setUpSideBar(PPanelContainer sideBar) {
        commandBar = new PPanelContainer();
        commandBar.setBounds(0, 60, sideBar.getWidth(), 117);
        commandBar.setLayout(new GridLayout(1, 4, 5, 0));
        sideBar.add(commandBar);

        keyNameContainer = new PPanelContainer();
        keyNameContainer.setBounds(0, mapContainer.getHeight() - 108, sideBar.getWidth(), 25);
        keyNameContainer.setLayout(new GridLayout(1, 3, 5, 5));
        sideBar.add(keyNameContainer);

        keyContainer = new PPanelContainer();
        keyContainer.setBounds(0, mapContainer.getHeight() - 83, sideBar.getWidth(), 159);
        keyContainer.setLayout(new GridLayout(1, 3, 5, 5));
        sideBar.add(keyContainer);
    }

    /**
     * Prepares the action buttons without adding them to the mapElement
     */
    private void prepareActionButtons() {
        actionButtons = new ArrayList<>();
        String[] fileNames = {"MOVE", "FLOOR", "DOME", "END"};
        for (String fileName : fileNames) {
            Image scaledImage = (new ImageIcon(getClass().getResource("/GuiResources/" + fileName + "_choice.png")))
                    .getImage().getScaledInstance(77, 117, Image.SCALE_SMOOTH);
            actionButtons.add(new PButton(scaledImage));
        }
    }

    /**
     * Builds and add the map element to the container in a default position
     *
     * @param mapInfo The information about the map
     */
    public void showMap(MapInfo mapInfo) {
        cells = new PButton[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                cells[i][j] = new PButton(mapInfo, i, j);
                cells[i][j].setSize(30, 65);
                mapContainer.add(cells[i][j]);
            }
        }
        updateKey(mapInfo);
    }

    /**
     * Updates the key
     *
     * @param mapInfo The new map info
     */
    private void updateKey(MapInfo mapInfo) {
        keyContainer.removeAll();
        keyNameContainer.removeAll();

        List<String> cards = mapInfo.getCards();
        List<String> names = mapInfo.getNicknames();
        List<String> colors = mapInfo.getColors();

        for (int i = 0; i < mapInfo.getNumPlayers(); i++) {
            Image scaledImage = Configurator.getCardImage(cards.get(i)).getScaledInstance(95, 159, Image.SCALE_SMOOTH);
            PButton cardButton = new PButton(scaledImage);
            keyContainer.add(cardButton);

            PLabel name = new PLabel(names.get(i));
            name.setFontSize(19);
            name.setForeground(PlayerColor.getColorCodeByName(colors.get(i)));
            keyNameContainer.add(name);
        }
    }

    /**
     * Sets the text of the map element heading
     *
     * @param heading The text to be shown
     */
    public void setHeading(String heading) {
        commandLabel.setText(heading);
    }

    /**
     * Enable the cell selection in all cells except specific ones for a specified genre
     *
     * @param forbiddenCells The set of forbidden cells
     * @param genre          The genre of the worker to be positioned
     */
    public void enableCellSelection(List<Cell> forbiddenCells, Genre genre) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Cell currentCell = new Cell(i, j);
                if (forbiddenCells.contains(currentCell)) {
                    cells[i][j].addActionListener((ev) -> (new Thread(() -> setErrorMessage("Invalid choice. Try again."))).start());
                } else {
                    cells[i][j].addActionListener((ev) -> (new Thread(() -> {
                        setErrorMessage("");
                        guiView.getServerHandler().sendPlayerPosition(genre, currentCell);
                    })).start());
                }
            }
        }
    }

    /**
     * Sets the error message
     *
     * @param errorMessage The error message
     */
    public void setErrorMessage(String errorMessage) {
        messageLabel.setText(errorMessage);
        messageLabel.setErrorForeground();
    }

    /**
     * Sets the state message
     *
     * @param message The state message
     */
    public void setStateMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setMessageForeground();
    }

    /**
     * Remove all the cells from the map
     */
    public void clearMap() {
        mapContainer.removeAll();
    }

    /**
     * Enable the commands buttons
     *
     * @param possibleActions The possible actions to be allowed
     */
    public void enableActions(RoundActions possibleActions) {
        if (!commandsVisibility)
            showCommands();

        setHeading("Choose your action");
        int index = 0;
        for (ActionType actionType : ActionType.values()) {
            if (!actionType.equals(ActionType.LOSE)) {
                if (possibleActions.contains(actionType)) {
                    actionButtons.get(index).addActionListener(new AllowedActionListener(actionType));
                } else {
                    actionButtons.get(index).addActionListener(new ForbiddenActionListener(actionType));
                }
                index++;
            }
        }
    }

    /**
     * Adds the commands to the commands container
     */
    private void showCommands() {
        for (PButton button : actionButtons) {
            commandBar.add(button);
        }
        commandsVisibility = true;
    }

    /**
     * Listener of not allowed actions
     */
    public class ForbiddenActionListener implements ActionListener {
        private final ActionType notAllowedType;

        /**
         * Constructor: build a forbidden action type listener
         *
         * @param notAllowedType The not allowed action type
         */
        public ForbiddenActionListener(ActionType notAllowedType) {
            this.notAllowedType = notAllowedType;
        }


        /**
         * Adds the error message to the state label
         *
         * @param e The event to be processed
         */
        public void actionPerformed(ActionEvent e) {
            setErrorMessage(notAllowedType.name() + " is not allowed");
        }
    }

    /**
     * Listener of allowed round actions
     */
    public class AllowedActionListener implements ActionListener {
        private final ActionType actionType;

        /**
         * Constructor: build a allowed action type listener
         *
         * @param actionType The allowed action type
         */
        public AllowedActionListener(ActionType actionType) {
            this.actionType = actionType;
        }


        /**
         * Invoked when an action occurs
         *
         * @param e The event to be processed
         */
        public void actionPerformed(ActionEvent e) {
            System.out.println(actionType);
        }
    }
}
