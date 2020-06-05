package client;

import client.gui.components.PButton;
import client.gui.components.PLabel;
import client.gui.components.PPanelBackground;
import client.gui.components.PPanelContainer;
import client.gui.elements.*;
import model.Card;
import model.Cell;
import model.Player;
import util.Configurator;
import util.Genre;
import util.MapInfo;
import util.RoundActions;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Manages the graphic user interface
 */
public class GuiView implements View {
    private ServerHandler serverHandler;
    private JFrame mainFrame;
    private PPanelContainer bodyContainer;
    private MapElement mapElement;
    private final InputValidator inputValidator;

    /**
     * Constructor: build a GuiView
     */
    public GuiView(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            ge.registerFont(Font.createFont(0, getClass().getResourceAsStream("/GuiResources/LeGourmetScript.otf")));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        inputValidator= new InputValidator();
    }

    /**
     * Sets the serverHandler
     *
     * @param serverHandler The serverHandler
     */
    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    /**
     * Gets the server handler that manages the graphical user interface view
     * @return  The serverHandler that manages the GuiView
     */
    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    /**
     * Interface launcher. Asks the server IP to connect to and notify it to the serverHandler
     */
    public void launch() {
        SwingUtilities.invokeLater(() -> {
            // Prepare the main frame
            mainFrame = new JFrame("Santorini");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(1000, 730);
            mainFrame.setResizable(false);

            Image backgroundImage = (new ImageIcon(getClass().getResource("/GuiResources/background.png"))).getImage();
            JPanel background = new PPanelBackground(backgroundImage);
            mainFrame.repaint();
            background.setLayout(null);
            mainFrame.add(background);

            // Prepare the body container
            bodyContainer = new PPanelContainer();
            bodyContainer.setBounds(80, 200, 840, 450);
            background.add(bodyContainer);

            FormElement form = new FormElement(bodyContainer);
            form.addField(0,"Enter the server IP: ",Configurator.getDefaultIp());

            form.setActionButton("NEXT",(ev) -> (new Thread(() -> {
                String serverIP = form.getFieldValueAt(0);
                if (inputValidator.isIp(serverIP) && !inputValidator.isEmptyIp(serverIP)) {
                    showQueuedMessage();
                    serverHandler.setConnection(serverIP);
                } else {
                    form.setErrorMessage("Invalid ip. Try again.");
                }
            })).start());

            mainFrame.setVisible(true);
        });
    }

    /**
     * Apply changes to a component
     * @param component The component
     */
    private void applyChangesTo(Component component) {
        component.revalidate();
        component.repaint();
    }

    /**
     * Flush the components inside a panel
     * @param panel     The panel
     */
    private void clear(JPanel panel){
        panel.removeAll();
    }

    /**
     * Flush the components inside a frame
     * @param frame     The frame
     */
    private void clear(JFrame frame){
        frame.getContentPane().removeAll();
    }

    /**
     * Shows a message to say to the user that is connected to
     * the server and will be added to the next available game
     */
    public void showQueuedMessage() {
        SwingUtilities.invokeLater(() -> {
            clear(bodyContainer);

            MessageElement messageElement=new MessageElement(bodyContainer);
            messageElement.setMessage("You will be added to the first available game...");

            applyChangesTo(bodyContainer);
        });
    }

    /**
     * Shows a specified message to the user
     *
     * @param message   The message to be shown
     * @param newScreen True if it's necessary to clean the interface
     */
    public void showMessage(String message, boolean newScreen) {
        SwingUtilities.invokeLater(() -> {
            if (newScreen) {
                clear(bodyContainer);

                MessageElement messageElement = new MessageElement(bodyContainer);
                messageElement.setMessage(message);

                applyChangesTo(bodyContainer);
            } else {
                mapElement.setStateMessage(message);
            }
        });
    }

    /**
     * Shows a loading message to the user
     */
    public void showLoading() {
        SwingUtilities.invokeLater(() -> {
            clear(bodyContainer);

            MessageElement messageElement=new MessageElement(bodyContainer);
            messageElement.setMessage("The game will start shortly, get ready!");

            applyChangesTo(bodyContainer);
        });
    }

    /**
     * Asks a new nickname to the user and notify the choice to the serverHandler
     */
    public void askNewNickname() {
        SwingUtilities.invokeLater(() -> {
            clear(bodyContainer);

            FormElement form = new FormElement(bodyContainer);
            form.addField(0,"Nickname: ","");
            form.setErrorMessage("The chosen username is already taken.");

            form.setActionButton("NEXT",(ev) -> (new Thread(() -> {
                String nickname = form.getFieldValueAt(0);
                if (inputValidator.isNickname(nickname)) {
                    showMessage("Waiting for the other players to connect...", true);
                    serverHandler.sendNewNickname(nickname);
                } else {
                    form.setErrorMessage("Invalid nickname. Try again.");
                }
            })).start());

            applyChangesTo(bodyContainer);
        });
    }

    /**
     * Asks nickname, birth date and if it's a new game the number of players
     * for the game and notify the information to the serverHandler
     *
     * @param newGame True if the it is a new game, otherwise false
     */
    public void setUpGame(boolean newGame) {
        SwingUtilities.invokeLater(() -> {
            clear(bodyContainer);

            FormElement form = new FormElement(bodyContainer);
            form.addField(0,"Nickname: ","");
            form.addField(1,"Date of birth [dd/mm/yyyy]: ","");
            form.addField((newGame)? 2 : -1,"Number of competitors [2..3]: ","");

            form.setActionButton("NEXT",(ev) -> (new Thread(() -> {
                String nickname = form.getFieldValueAt(0);
                if (!inputValidator.isNickname(nickname)) {
                    form.setErrorMessage("Invalid nickname. Try again.");
                    return;
                }

                Date date=inputValidator.isDate(form.getFieldValueAt(1));
                if(date==null){
                    form.setErrorMessage("Invalid date. Try again.");
                    return;
                }

                Integer playersNumber=0;
                if (newGame) {
                    playersNumber= inputValidator.isNumPlayer(form.getFieldValueAt(2));
                    if(playersNumber==null) {
                        form.setErrorMessage("Invalid choice. Try again.");
                        return;
                    }
                }

                showMessage("Waiting for the other players to connect...", true);
                serverHandler.sendSetUpGame(nickname, date, playersNumber);
            })).start());

            applyChangesTo(bodyContainer);
        });
    }

    /**
     * Asks the game cards
     *
     * @param numCards Number of cards to be selected
     */
    public void askGameCards(int numCards) {
        List<Card> chosenCard = new ArrayList<>();
        printCards(chosenCard, numCards);
    }

    /**
     * Adds the card switcher to the view
     * @param chosenCards   The cards not to be shown
     * @param numCards      The number of cards to be shown
     */
    public void printCards(List<Card> chosenCards, int numCards) {
        if (numCards == chosenCards.size()) {
            (new Thread(() -> {
                showLoading();
                serverHandler.sendGameCards(chosenCards);
            })).start();
        } else {
            SwingUtilities.invokeLater(() -> {
                clear(bodyContainer);

                CardSwitcher cardSwitcher= new CardSwitcher(bodyContainer,this);
                cardSwitcher.setHeading("Choose " + (numCards - chosenCards.size()) + " card" + (numCards - chosenCards.size() > 1 ? "s" : "") + " between these:");
                cardSwitcher.showSwitcher(chosenCards,numCards);
                cardSwitcher.showCardDetails();

                applyChangesTo(bodyContainer);
            });
        }
    }

    /**
     * Asks the card the player whats to use during the game
     *
     * @param possibleChoices All the possible cards
     */
    public void askPlayerCard(List<Card> possibleChoices) {
        SwingUtilities.invokeLater(() -> {
            clear(bodyContainer);

            CardSwitcher cardSwitcher= new CardSwitcher(bodyContainer,this);
            cardSwitcher.setHeading("Choose your card between these:");
            cardSwitcher.showSwitcher(possibleChoices,true);
            cardSwitcher.showCardDetails();

            applyChangesTo(bodyContainer);
        });
    }

    /**
     * Asks the nickname of the first player
     *
     * @param playersNicknames All the nicknames
     */
    public void askFirstPlayer(List<String> playersNicknames) {
        SwingUtilities.invokeLater(() -> {
            clear(bodyContainer);

            PlayerSwitcher playerSwitcher=new PlayerSwitcher(bodyContainer,this);
            playerSwitcher.showHeading("Choose the first player:");
            playerSwitcher.showSwitcher(playersNicknames);

            applyChangesTo(bodyContainer);
        });
    }

    /**
     * Show all the cards of the game
     *
     * @param cards All the cards of the game
     */
    public void showGameCards(List<Card> cards) {
        SwingUtilities.invokeLater(() -> {
            clear(bodyContainer);

            CardSwitcher cardSwitcher= new CardSwitcher(bodyContainer,this);
            cardSwitcher.setHeading("The cards used in this match will be:");
            cardSwitcher.showSwitcher(cards,false);
            cardSwitcher.showCardDetails();

            applyChangesTo(bodyContainer);
        });
    }


    /**
     * Shows the cards assignment of the game
     *
     * @param playerList The list of players of the game
     */
    public void showCardAssignmentMessage(List<Player> playerList) {
        SwingUtilities.invokeLater(() -> {
            clear(bodyContainer);

            List<Card> cards= new ArrayList<>();
            List<String> owners= new ArrayList<>();
            for(Player player: playerList){
                cards.add(player.getCard());
                owners.add(player.getNickname());
            }

            CardSwitcher cardSwitcher= new CardSwitcher(bodyContainer,this);
            cardSwitcher.setHeading("Cards assignment:");
            cardSwitcher.showSwitcher(cards,false);
            cardSwitcher.setSingleCardLabel(owners);

            applyChangesTo(bodyContainer);
        });
    }

    /**
     * Asks the color the player whats to choose
     *
     * @param availableColors All the available colors
     */
    public void askPlayerColor(ArrayList<String> availableColors) {
        if (availableColors.size() == 1) {
            (new Thread(() -> serverHandler.sendPlayerColor(availableColors.get(0)))).start();
        } else {
            SwingUtilities.invokeLater(() -> {
                clear(bodyContainer);

                ColorSwitcher colorSwitcher = new ColorSwitcher(bodyContainer, this);
                colorSwitcher.setHeading("Choose your color between these:");
                colorSwitcher.showSwitcher(availableColors);

                applyChangesTo(bodyContainer);
            });
        }
    }


    /**
     * Asks the first position for the male and female worker
     *
     * @param genre          The genre of the worker
     * @param forbiddenCells The forbidden cells
     * @param mapInfo        The map info
     */
    public void askPlayerPosition(Genre genre, List<Cell> forbiddenCells, MapInfo mapInfo) {
        SwingUtilities.invokeLater(() -> {
            enableGameMode();

            mapElement = new MapElement(bodyContainer, this);
            mapElement.showMap(mapInfo);
            mapElement.setHeading("Choose the position of your worker");
            mapElement.enableFirstPositionSelection(forbiddenCells, genre);

            applyChangesTo(bodyContainer);
        });
    }

    /**
     * Enable full screen game mode, reducing the space occupied by the santorini logo
     */
    private void enableGameMode() {
        clear(mainFrame);

        Image backgroundImage = (new ImageIcon(getClass().getResource("/GuiResources/emptyBackground.png"))).getImage();
        JPanel background = new PPanelBackground(backgroundImage);
        mainFrame.repaint();
        background.setLayout(null);
        mainFrame.add(background);

        // Prepare the body container
        bodyContainer = new PPanelContainer();
        bodyContainer.setBounds(80, 60, 840, 610);
        background.add(bodyContainer);

        applyChangesTo(mainFrame);
    }


    /**
     * Shows the board of the game
     *
     * @param mapInfo   The map info
     * @param newScreen True if it's necessary to clean the interface
     */
    public void showMap(MapInfo mapInfo, boolean newScreen) {
        SwingUtilities.invokeLater(() -> {
            if (mapElement == null) {
                enableGameMode();
                mapElement = new MapElement(bodyContainer, this);
            } else {
                mapElement.clearMap();
            }
            mapElement.showMap(mapInfo);

            applyChangesTo(bodyContainer);
        });
    }

    /**
     * Asks the action the player wants to perform
     *
     * @param roundActions  All the possible actions
     * @param mapInfo       The map info
     * @param loserNickname The nickname of the looser or null value
     */
    public void askAction(RoundActions roundActions, MapInfo mapInfo, String loserNickname) {
        SwingUtilities.invokeLater(() -> {
            showMap(mapInfo, false);
            if (loserNickname != null) {
                showLoser(loserNickname);
            }
            mapElement.enableActionTypeSelection(roundActions);

            applyChangesTo(bodyContainer);
        });
    }

    /**
     * Notify the players that the game has ended and notify the winner
     *
     * @param winnerNickname The nickname of the winner
     * @param youWin         True if the player has win
     */
    public void showGameEndMessage(String winnerNickname, boolean youWin) {
        SwingUtilities.invokeLater(() -> {
            mapElement.cleanSideBar();
            mapElement.setHeading(winnerNickname + " is the winner!");

            Image playAgain = (new ImageIcon(getClass().getResource("/GuiResources/play_again.png")))
                    .getImage().getScaledInstance(159, 159, Image.SCALE_SMOOTH);
            PButton playAgainButton = new PButton(playAgain);
            playAgainButton.setBounds(598, 410, 159, 159);
            bodyContainer.add(playAgainButton);
            playAgainButton.addActionListener((ev) -> {
                mainFrame.dispose();
                (new Thread(() -> serverHandler.sendNewGame(true))).start();
            });

            Image endGameScaled;
            String message;
            if (youWin) {
                endGameScaled = (new ImageIcon(getClass().getResource("/GuiResources/win.png")))
                        .getImage().getScaledInstance(184, 260, Image.SCALE_SMOOTH);
                message = "Congratulations!";
            } else {
                endGameScaled = (new ImageIcon(getClass().getResource("/GuiResources/lose.png")))
                        .getImage().getScaledInstance(184, 260, Image.SCALE_SMOOTH);
                message = "You lose!";
            }

            PButton endGame = new PButton(endGameScaled);
            endGame.setBounds(586, 140, 184, 260);
            PLabel endMessage = new PLabel(message);
            endMessage.setFontSize(20);
            endMessage.setBounds(27, 19, 130, 21);
            endGame.setLayout(null);
            endGame.add(endMessage);
            bodyContainer.add(endGame);

        });
    }

    /**
     * Notify the players that a player has disconnected and the game has ended
     *
     * @param disconnectedNickname The nickname of the disconnected player
     */
    public void showDisconnectionMessage(String disconnectedNickname) {
        SwingUtilities.invokeLater(() -> {
            clear(bodyContainer);

            PLabel label = new PLabel("GAME OVER: " + disconnectedNickname + " has disconnected.");
            label.setBounds(0, 0, 840, 450 / 2);
            bodyContainer.add(label);

            Image playAgain = (new ImageIcon(getClass().getResource("/GuiResources/play_again.png")))
                    .getImage().getScaledInstance(159, 159, Image.SCALE_SMOOTH);
            PButton playAgainButton = new PButton(playAgain);
            playAgainButton.setBounds(340, 450 / 2, 159, 159);
            bodyContainer.add(playAgainButton);
            playAgainButton.addActionListener((ev) -> {
                mainFrame.dispose();
                (new Thread(() -> serverHandler.sendNewGame(true))).start();
            });
            applyChangesTo(bodyContainer);
        });
    }

    /**
     * Shows an error message to the user
     *
     * @param errorMessage The message to be shown
     * @param newScreen    True if it's necessary to clean the interface
     */
    public void showErrorMessage(String errorMessage, boolean newScreen) {
        SwingUtilities.invokeLater(() -> {
            clear(bodyContainer);

            PLabel label = new PLabel(errorMessage);
            label.setBounds(0, 0, 840, 450);
            label.setForeground(Color.RED);
            bodyContainer.add(label);

            applyChangesTo(bodyContainer);
        });
    }

    /**
     * Notify the players that there is a loser
     *
     * @param loserNickname The nickname of the loser
     */
    public void showLoser(String loserNickname) {
        showMessage(loserNickname.toUpperCase() + " has lost ", false);
    }

    /**
     * Shows the user who is taking his turn
     *
     * @param currentNickname The nickname of the user who is taking his turn
     * @param hasLoser        True if during the turn a player has lost
     */
    public void showTurn(String currentNickname, boolean hasLoser) {
        SwingUtilities.invokeLater(() -> mapElement.setHeading("It's " + currentNickname.toUpperCase() + " turn"));
    }

}
