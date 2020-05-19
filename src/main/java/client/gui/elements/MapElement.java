package client.gui.elements;

import client.GuiView;
import client.gui.components.*;
import model.Cell;
import util.Action;
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
    private final PPanelContainer sideBar;

    private MapInfo mapInfo;
    private Boolean actionPerformed;
    private RoundActions possibleActions;
    private ActionType selectedActionType;
    private Genre selectedGenre;
    private boolean myTurn;

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

        sideBar = new PPanelContainer();
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
        this.mapInfo = mapInfo;
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
    public void enableFirstPositionSelection(List<Cell> forbiddenCells, Genre genre) {
        actionPerformed = false;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Cell currentCell = new Cell(i, j);
                if (!forbiddenCells.contains(currentCell)) {
                    cells[i][j].addActionListener(new FirstPositionListener(genre, currentCell));
                } else {
                    cells[i][j].addActionListener(new FirstPositionListener());
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
    public void enableActionTypeSelection(RoundActions possibleActions) {
        if (!commandsVisibility)
            showCommands();
        setErrorMessage("");

        this.myTurn = true;
        this.possibleActions = possibleActions;
        this.selectedActionType = null;
        setHeading("Choose your action");
        int index = 0;
        for (ActionType actionType : ActionType.values()) {
            if (!actionType.equals(ActionType.LOSE)) {
                removeAllActionListener(actionButtons.get(index));
                boolean allowed = possibleActions.contains(actionType);
                actionButtons.get(index).addActionListener(new ActionTypeListener(actionType, allowed));
                index++;
            }
        }
    }

    public void enableGenreSelection(ActionType actionType) {
        this.selectedActionType = actionType;
        this.selectedGenre = null;
        if (actionType == ActionType.END) {
            (new Thread(() -> guiView.getServerHandler().sendAction(possibleActions.findEnd()))).start();
            return;
        }

        setHeading("Select one of your workers");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                removeAllActionListener(cells[i][j]);
                String currentColor = mapInfo.getColorAt(i, j);
                boolean allowedWorker = currentColor != null && currentColor.equalsIgnoreCase(mapInfo.getCurrentPlayerColor());
                if (allowedWorker) {
                    Genre currentGenre = mapInfo.getGenreAt(i, j);
                    if (possibleActions.findGenre(selectedActionType, currentGenre)) {
                        cells[i][j].addActionListener(new GenreListener(currentGenre, i, j));
                    } else {
                        cells[i][j].addActionListener(new GenreListener());
                    }
                } else {
                    cells[i][j].addActionListener(new GenreListener());
                }
            }
        }
    }

    private void enableFinalCell(Genre genre, int row, int column) {
        this.selectedGenre = genre;
        this.actionPerformed = false;

        setHeading("Select one of neighboring cells");
        Cell workerCell = new Cell(row, column);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                removeAllActionListener(cells[i][j]);
                Cell currentCell = new Cell(i, j);
                Direction direction = workerCell.calculateDirection(currentCell);
                Action theAction = possibleActions.find(selectedActionType, selectedGenre, direction);
                if (theAction != null) {
                    cells[i][j].addActionListener(new FinalCellListener(theAction));
                } else {
                    cells[i][j].addActionListener(new FinalCellListener());
                }
            }
        }
    }

    private void removeAllActionListener(PButton pButton) {
        for (ActionListener act : pButton.getActionListeners()) {
            pButton.removeActionListener(act);
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

    public void cleanSideBar() {
        sideBar.removeAll();

    }

    public class ActionTypeListener implements ActionListener {
        private final ActionType actionType;
        private final boolean allowed;

        public ActionTypeListener(ActionType actionType, boolean allowed) {
            this.actionType = actionType;
            this.allowed = allowed;
        }

        public void actionPerformed(ActionEvent e) {
            if (!myTurn) {
                setErrorMessage("It's not your turn!");
                return;
            }
            if (selectedActionType == null) {
                if (allowed) {
                    setErrorMessage("");
                    enableGenreSelection(actionType);
                } else {
                    setErrorMessage(actionType.name() + " is not allowed");
                }
            } else {
                setErrorMessage("You have already chosen your action type");
            }
        }
    }

    public class GenreListener implements ActionListener {
        private final boolean selectable;
        private Genre genre;
        private int row, column;

        public GenreListener(Genre genre, int row, int column) {
            this.genre = genre;
            this.row = row;
            this.column = column;
            this.selectable = true;
        }

        public GenreListener() {
            this.selectable = false;
        }

        /**
         * Invoked when an action occurs
         *
         * @param e The event to be processed
         */
        public void actionPerformed(ActionEvent e) {
            if (selectedGenre == null) {
                if (selectable) {
                    setErrorMessage("");
                    enableFinalCell(genre, row, column);
                } else {
                    setErrorMessage("Not allowed selection");
                }
            } else {
                setErrorMessage("You have already chosen your worker");
            }
        }
    }

    public class FinalCellListener implements ActionListener {
        private final boolean selectable;
        private Action action;

        public FinalCellListener(Action action) {
            this.action = action;
            this.selectable = true;
        }

        public FinalCellListener() {
            this.selectable = false;
        }

        /**
         * Invoked when an action occurs
         *
         * @param e The event to be processed
         */
        public synchronized void actionPerformed(ActionEvent e) {
            if (!actionPerformed) {
                if (selectable) {
                    actionPerformed = true;
                    myTurn = false;
                    setErrorMessage("");
                    (new Thread(() -> guiView.getServerHandler().sendAction(action))).start();
                } else {
                    setErrorMessage("Invalid choice. Try again.");
                }
            }
        }
    }

    public class FirstPositionListener implements ActionListener {
        private final boolean selectable;
        private Genre genre;
        private Cell currentCell;

        public FirstPositionListener(Genre genre, Cell currentCell) {
            this.genre = genre;
            this.currentCell = currentCell;
            this.selectable = true;
        }

        public FirstPositionListener() {
            this.selectable = false;
        }

        /**
         * Invoked when an action occurs.
         *
         * @param e the event to be processed
         */
        public void actionPerformed(ActionEvent e) {
            if (!actionPerformed) {
                if (!selectable) {
                    setErrorMessage("Invalid choice. Try again.");
                } else {
                    actionPerformed = true;
                    setErrorMessage("");
                    (new Thread(() -> guiView.getServerHandler().sendPlayerPosition(genre, currentCell))).start();
                }
            }
        }
    }
}
