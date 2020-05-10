package client;

import client.guiComponents.*;
import model.*;
import util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private PPanelContainer commandBar;

    /**
     * Constructor: build a GuiView
     */
    public GuiView(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            ge.registerFont(Font.createFont(0, new File(getClass().getResource("/GuiResources/LeGourmetScript.otf").getFile())));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
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

            Form form=new Form(bodyContainer);
            form.addField(0,"Enter the server IP: ",Configurator.getDefaultIp());

            form.setActionButton("NEXT",(ev) -> (new Thread(() -> {
                showQueuedMessage();
                serverHandler.setConnection(form.getFieldValueAt(0));
            })).start());

            mainFrame.setVisible(true);
        });
    }

    /**
     * Apply changes to a panel
     * @param panel The panel
     */
    private void applyChangesTo(JPanel panel) {
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Flush the components inside a panel
     * @param panel     The panel
     */
    private void clear(JPanel panel){
        panel.removeAll();
    }

    /**
     * Shows a message to say to the user that is connected to
     * the server and will be added to the next available game
     */
    public void showQueuedMessage() {
        SwingUtilities.invokeLater(() -> {
            showMessage("You will be added to the first available game...",true);
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
            clear(bodyContainer);

            PLabel label = new PLabel(message);
            label.setBounds(0, 0, 840, 450);
            bodyContainer.add(label);

            applyChangesTo(bodyContainer);
        });
    }

    /**
     * Shows a loading message to the user
     */
    public void showLoading() {
        SwingUtilities.invokeLater(() -> {
            clear(bodyContainer);

            PLabel label = new PLabel("The game will start shortly, get ready!");
            label.setBounds(0, 0, 840, 450);
            bodyContainer.add(label);

            applyChangesTo(bodyContainer);
        });
    }

    /**
     * Asks a new nickname to the user and notify the choice to the serverHandler
     */
    public void askNewNickname() {
        SwingUtilities.invokeLater(() -> {
            clear(bodyContainer);

            Form form=new Form(bodyContainer);
            form.addField(0,"Nickname: ","");
            form.addErrorLabel("The chosen username is already taken.");

            form.setActionButton("NEXT",(ev) -> (new Thread(() -> {
                String nickname = form.getFieldValueAt(0);
                if (!nickname.equals("") && !nickname.contains(" ")) {
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

            Form form= new Form(bodyContainer);
            form.addField(0,"Nickname: ","");
            form.addField(1,"Date of birth [dd/mm/yyyy]: ","");
            form.addField((newGame)? 2 : -1,"Number of competitors [2..3]: ","");
            form.addErrorLabel("");

            form.setActionButton("NEXT",(ev) -> (new Thread(() -> {
                String nickname = form.getFieldValueAt(0);
                if (nickname.equals("") || nickname.contains(" ")) {
                    form.setErrorMessage("Invalid nickname. Try again.");
                    return;
                }

                Date date;
                try {
                    date = (new SimpleDateFormat("dd/MM/yyyy")).parse(form.getFieldValueAt(1));
                } catch (ParseException e) {
                    form.setErrorMessage("Invalid date. Try again.");
                    return;
                }

                int playersNumber=0;
                if (newGame) {
                    try {
                        playersNumber = Integer.parseInt(form.getFieldValueAt(2));
                    } catch (Exception e) {
                        form.setErrorMessage("Invalid choice. Try again.");
                        return;
                    }

                    if (playersNumber < 2 || playersNumber > 3) {
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

    private void printCards(List<Card> chosenCards, int numCards) {
        if (numCards == chosenCards.size()) {
            (new Thread(() -> {
                showLoading();
                serverHandler.sendGameCards(chosenCards);
            })).start();
        } else {
            SwingUtilities.invokeLater(() -> {
                // Flush body components
                this.bodyContainer.removeAll();

                List<Card> allCardList = Configurator.getAllCards();

                // Remove cards
                allCardList.removeAll(chosenCards);

                PLabel label = new PLabel("Choose " + (numCards - chosenCards.size()) + " card" + (numCards - chosenCards.size() > 1 ? "s" : "") + " between these:");
                label.setFontSize(30);
                label.setBounds(0, 0, 840, 40);
                bodyContainer.add(label);

                PPanelContainer cardContainer = new PPanelContainer();
                cardContainer.setBounds(0, 80, bodyContainer.getWidth(), 138);
                cardContainer.setLayout(new GridLayout(1, 9, 10, 0));
                bodyContainer.add(cardContainer);

                PPanelContainer descriptionBackground = new PPanelContainer();
                descriptionBackground.setBounds(0, 280, bodyContainer.getWidth(), 150);
                descriptionBackground.setLayout(null);
                bodyContainer.add(descriptionBackground);

                PLabel nameLabel = new PLabel("");
                nameLabel.setFontSize(30);
                nameLabel.setForeground(new Color(186, 164, 154));
                nameLabel.setBounds(0, 20, descriptionBackground.getWidth(), 35);
                descriptionBackground.add(nameLabel);

                PLabel descriptionLabel = new PLabel("");
                descriptionLabel.setFont(new Font("Helvetica", Font.PLAIN, 17));
                descriptionLabel.setBounds(90, 50, descriptionBackground.getWidth() - 180, 80);
                descriptionBackground.add(descriptionLabel);

                for (Card card : allCardList) {
                    Image scaledImage = Configurator.getCardImage(card.getName()).getScaledInstance(82, 138, Image.SCALE_SMOOTH);
                    PButton cardButton = new PButton(scaledImage);
                    cardContainer.add(cardButton);
                    cardButton.addMouseListener(new GuiView.CardMouseListener(card, nameLabel, descriptionLabel, chosenCards, numCards));
                }

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
            // Flush body components
            this.bodyContainer.removeAll();

            PLabel label = new PLabel("Choose your card between these:");
            label.setFontSize(30);
            label.setBounds(0, 0, 840, 40);
            bodyContainer.add(label);

            PPanelContainer cardContainer = new PPanelContainer();
            cardContainer.setBounds(0, 80, bodyContainer.getWidth(), 138);
            cardContainer.setLayout(new GridLayout(1, 9, 10, 0));
            bodyContainer.add(cardContainer);

            PPanelContainer descriptionBackground = new PPanelContainer();
            descriptionBackground.setBounds(0, 280, bodyContainer.getWidth(), 150);
            descriptionBackground.setLayout(null);
            bodyContainer.add(descriptionBackground);

            PLabel nameLabel = new PLabel("");
            nameLabel.setFontSize(30);
            nameLabel.setForeground(new Color(186, 164, 154));
            nameLabel.setBounds(0, 20, descriptionBackground.getWidth(), 35);
            descriptionBackground.add(nameLabel);

            PLabel descriptionLabel = new PLabel("");
            descriptionLabel.setFont(new Font("Helvetica", Font.PLAIN, 17));
            descriptionLabel.setBounds(90, 50, descriptionBackground.getWidth() - 180, 80);
            descriptionBackground.add(descriptionLabel);

            for (Card card : possibleChoices) {
                Image scaledImage = Configurator.getCardImage(card.getName()).getScaledInstance(82, 138, Image.SCALE_SMOOTH);
                PButton cardButton = new PButton(scaledImage);
                cardContainer.add(cardButton);
                cardButton.addMouseListener(new GuiView.CardMouseListener(card, nameLabel, descriptionLabel, true));
            }

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
            // Flush body components
            this.bodyContainer.removeAll();

            PLabel label = new PLabel("Choose the first player:");
            label.setFontSize(30);
            label.setBounds(0, 0, 840, 40);
            bodyContainer.add(label);

            PPanelContainer playerContainer = new PPanelContainer();
            playerContainer.setBounds(0, 80, bodyContainer.getWidth(), 138);
            playerContainer.setLayout(new GridLayout(1, playersNicknames.size(), 10, 0));
            bodyContainer.add(playerContainer);

            for (String name : playersNicknames) {
                Image scaledImage = (new ImageIcon(getClass().getResource("/GuiResources/btn_blue.png"))).getImage().getScaledInstance(197, 50, Image.SCALE_SMOOTH);
                PButtonSigned button = new PButtonSigned(scaledImage,name);
                button.addActionListener((ev) -> (new Thread(() -> {
                    showLoading();
                    serverHandler.sendFirstPlayer(button.getSign());
                })).start());
                playerContainer.add(button);
            }

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
            // Flush body components
            this.bodyContainer.removeAll();

            PLabel label = new PLabel("The cards used in this match will be:");
            label.setFontSize(30);
            label.setBounds(0, 0, 840, 40);
            bodyContainer.add(label);

            PPanelContainer cardContainer = new PPanelContainer();
            cardContainer.setBounds(0, 80, bodyContainer.getWidth(), 138);
            cardContainer.setLayout(new GridLayout(1, 9, 10, 0));
            bodyContainer.add(cardContainer);

            PPanelContainer descriptionBackground = new PPanelContainer();
            descriptionBackground.setBounds(0, 280, bodyContainer.getWidth(), 150);
            descriptionBackground.setLayout(null);
            bodyContainer.add(descriptionBackground);

            PLabel nameLabel = new PLabel("");
            nameLabel.setFontSize(30);
            nameLabel.setForeground(new Color(186, 164, 154));
            nameLabel.setBounds(0, 20, descriptionBackground.getWidth(), 35);
            descriptionBackground.add(nameLabel);

            PLabel descriptionLabel = new PLabel("");
            descriptionLabel.setFont(new Font("Helvetica", Font.PLAIN, 17));
            descriptionLabel.setBounds(90, 50, descriptionBackground.getWidth() - 180, 80);
            descriptionBackground.add(descriptionLabel);

            for (Card card : cards) {
                Image scaledImage = Configurator.getCardImage(card.getName()).getScaledInstance(82, 138, Image.SCALE_SMOOTH);
                PButton cardButton = new PButton(scaledImage);
                cardContainer.add(cardButton);
                cardButton.addMouseListener(new GuiView.CardMouseListener(card, nameLabel, descriptionLabel, false));
            }

            applyChangesTo(bodyContainer);
        });
    }

    /**
     * Shows th cards assignment of the game
     *
     * @param playerList The list of players of the game
     */
    public void showCardAssignmentMessage(List<Player> playerList) {
        SwingUtilities.invokeLater(() -> {
            // Flush body components
            this.bodyContainer.removeAll();

            PLabel label = new PLabel("Cards assignment:");
            label.setFontSize(30);
            label.setBounds(0, 0, 840, 40);
            bodyContainer.add(label);

            PPanelContainer cardContainer = new PPanelContainer();
            cardContainer.setBounds(0, 80, bodyContainer.getWidth(), 138);
            cardContainer.setLayout(new GridLayout(1, 9, 10, 0));
            bodyContainer.add(cardContainer);

            PPanelContainer assignmentContainer = new PPanelContainer();
            assignmentContainer.setBounds(0, 228, bodyContainer.getWidth(), 40);
            assignmentContainer.setLayout(new GridLayout(1, 9, 10, 0));
            bodyContainer.add(assignmentContainer);

            for (Player player : playerList) {
                Image scaledImage = Configurator.getCardImage(player.getCard().getName()).getScaledInstance(82, 138, Image.SCALE_SMOOTH);
                PButton cardButton = new PButton(scaledImage);
                cardContainer.add(cardButton);

                PLabel nameLabel = new PLabel(player.getNickname());
                nameLabel.setFontSize(25);
                nameLabel.setForeground(new Color(186, 164, 154));
                assignmentContainer.add(nameLabel);
            }

            applyChangesTo(bodyContainer);
        });
    }

    /**
     * Asks the color the player whats to choose
     *
     * @param availableColors All the available colors
     */
    public void askPlayerColor(List<String> availableColors) {
        SwingUtilities.invokeLater(() -> {
            // Flush body components
            this.bodyContainer.removeAll();

            PLabel label = new PLabel("Choose your color between these:");
            label.setFontSize(30);
            label.setBounds(0, 0, 840, 40);
            bodyContainer.add(label);

            PPanelContainer colorContainer = new PPanelContainer();
            colorContainer.setBounds(0, 80, bodyContainer.getWidth(), 138);
            colorContainer.setLayout(new GridLayout(1, 3, 10, 0));
            bodyContainer.add(colorContainer);

            for (String color : availableColors) {
                PButton button = new PButton(PlayerColor.getColorCodeByName(color));
                colorContainer.add(button);
                button.addActionListener((ev) -> (new Thread(() -> {
                    serverHandler.sendPlayerColor(color);
                    System.out.println("OK");
                })).start());
            }

            applyChangesTo(bodyContainer);
        });
    }

    /**
     * Asks the first position for the male and female worker
     *  @param genre            The genre of the worker
     * @param forbiddenCells    The forbidden cells
     * @param mapInfo           The map info
     */
    public void askPlayerPosition(Genre genre, List<Cell> forbiddenCells, MapInfo mapInfo) {
        mainFrame.getContentPane().removeAll();
        mainFrame.setVisible(true);

        Image backgroundImage = (new ImageIcon(getClass().getResource("/GuiResources/emptyBackground.png"))).getImage();
        JPanel background = new PPanelBackground(backgroundImage);
        mainFrame.repaint();
        background.setLayout(null);
        mainFrame.add(background);

        // Prepare the body container
        bodyContainer = new PPanelContainer();
        bodyContainer.setBounds(80, 60, 840, 610);
        background.add(bodyContainer);

        mainFrame.revalidate();
        mainFrame.repaint();

        showMap(mapInfo, false);

        PLabel commandLabel = new PLabel("Choose the position of your worker");
        commandLabel.setFontSize(30);
        commandLabel.setBounds(0, 0, bodyContainer.getWidth(), 40);
        bodyContainer.add(commandLabel);

        /*Cell chosenCell;
        String chosenPosition;
        int row = -1, column = -1;
        boolean incorrect;
        Frmt.clearScreen();
        do {
            showMap(game, false);
            incorrect = false;
            System.out.print("\n " + Frmt.style('b', "Choose the position of your " + ((genre == MALE) ? "male" : "female") + " worker [row,column]:") + " ");
            chosenPosition = scanner.next();
            try {
                row = Character.getNumericValue(chosenPosition.charAt(0));
                column = Character.getNumericValue(chosenPosition.charAt(2));
            } catch (Exception e) {
                Frmt.clearScreen();
                System.out.println(Frmt.color('r', "  > Invalid choice. Try again."));
                incorrect = true;
            }

            chosenCell = new Cell(row, column);
            if (!incorrect && (row < 0 || column < 0 || row > 4 || column > 4 || forbiddenCells.contains(chosenCell) || chosenPosition.length() != 3)) {
                Frmt.clearScreen();
                System.out.println(Frmt.color('r', "  > Invalid choice. Try again."));
                incorrect = true;
            }
        } while (incorrect);
        serverHandler.sendPlayerPosition(genre, chosenCell);*/
    }

    /**
     * Shows the board of the game
     *
     * @param mapInfo   The map info
     * @param newScreen True if it's necessary to clean the interface
     */
    public void showMap(MapInfo mapInfo, boolean newScreen) {
        SwingUtilities.invokeLater(() -> {
            if(newScreen) {
                // Flush body components
                this.bodyContainer.removeAll();
            }

            Image islandImg = (new ImageIcon(getClass().getResource("/GuiResources/SantoriniBoard.png"))).getImage();
            JPanel island = new PPanelBackground(islandImg);
            island.setBounds(0,50,496,500);
            island.repaint();
            island.setLayout(null);
            bodyContainer.add(island);

            PPanelContainer mapContainer= new PPanelContainer();
            mapContainer.setBounds(70,63,368,368);
            mapContainer.setLayout(new GridLayout(5,5,11,11));
            island.add(mapContainer);

            PPanelContainer sideBar= new PPanelContainer();
            sideBar.setBounds(island.getWidth()+20,63,324,450);
            sideBar.setLayout(new GridLayout(2,2,15,15));
            bodyContainer.add(sideBar);

            commandBar= new PPanelContainer();
            commandBar.setBounds(30,103,154,220);
            commandBar.setLayout(new GridLayout(2,2,20,20));
            sideBar.add(commandBar);

            PPanelContainer keyContainer= new PPanelContainer();
            keyContainer.setBounds(30,103,sideBar.getWidth(),175);
            keyContainer.setLayout(new GridLayout(1,3,5,5));
            sideBar.add(keyContainer);

            for(int i=0;i<5;i++){
                for(int j=0;j<5;j++){
                    PButton cell = new PButton(mapInfo,i,j);
                    cell.setSize(30,65);
                    mapContainer.add(cell);
                }
            }



            applyChangesTo(bodyContainer);
        });
    }

    /**
     * Asks the action the player wants to perform
     * @param roundActions  All the possible actions
     * @param mapInfo       The map info
     * @param loserNickname The nickname of the looser or null value
     */
    public void askAction(RoundActions roundActions, MapInfo mapInfo, String loserNickname) {

    }

    /**
     * Notify the players that the game has ended and notify the winner
     *
     * @param winnerNickname The nickname of the winner
     * @param youWin         True if the player has win
     */
    public void showGameEndMessage(String winnerNickname, boolean youWin) {

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
            label.setBounds(0, 0, 840, 450);
            bodyContainer.add(label);
            // TODO: mettere new game
            //serverHandler.sendNewGame(choice.equalsIgnoreCase("yes"));

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

    class CardMouseListener implements MouseListener {
        private final Card card;
        private final PLabel nameLabel;
        private final PLabel descriptionLabel;
        private final boolean gameCardsSetUp;
        private final boolean selectionEnabled;
        private int numCards;
        private List<Card> chosenCards;

        public CardMouseListener(Card card, PLabel nameLabel, PLabel descriptionLabel, List<Card> chosenCards, int numCards) {
            this.gameCardsSetUp = true;
            this.selectionEnabled = true;
            this.card = card;
            this.nameLabel = nameLabel;
            this.descriptionLabel = descriptionLabel;
            this.chosenCards = chosenCards;
            this.numCards = numCards;
        }

        public CardMouseListener(Card card, PLabel nameLabel, PLabel descriptionLabel, boolean selectionEnabled) {
            this.gameCardsSetUp = false;
            this.selectionEnabled = selectionEnabled;
            this.card = card;
            this.nameLabel = nameLabel;
            this.descriptionLabel = descriptionLabel;
        }

        public void mouseClicked(MouseEvent e) {
            if (gameCardsSetUp) {
                chosenCards.add(card);
                printCards(chosenCards, numCards);
            } else {
                if (selectionEnabled) {
                    showLoading();
                    serverHandler.sendPlayerCard(card);
                }
            }
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
            nameLabel.setText(card.getName().toUpperCase());
            String description = card.getDescription();
            descriptionLabel.setText("<HTML><CENTER>" + description + "</CENTER></HTML>");
        }

        public void mouseExited(MouseEvent e) {
            nameLabel.setText("");
            descriptionLabel.setText("");
        }
    }
}
